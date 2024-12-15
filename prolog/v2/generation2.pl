:- use_module(library(clpz)).
:- use_module(library(dcgs)).
:- use_module(library(reif)).
:- use_module(library(pairs)).
:- use_module(library(lists)).
:- use_module(library(format)).
:- use_module(library(error)).
:- use_module(library(pio)).
:- use_module(library(sgml)).
:- use_module(library(xpath)).
:- use_module(library(iso_ext)).
:- use_module(library(files)).
:- use_module(library(random)).
:- use_module(library(between)).

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  Основная логика составления расписаний.
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

find_lenght(NewLength, [Lesson | Lessons]) :- 
    group_subject_teacher_times(_, Lesson, _, T),
    NewLength #= Length + T,
    find_lenght(Length, Lessons).
find_lenght(T, [Lesson]) :-
    group_subject_teacher_times(_, Lesson, _, T).

main :-
    findall(Lesson, group_subject_teacher_times(_, Lesson, _, _), AllLessons),
    find_lenght(Length, AllLessons),
    ucs_traverse([node([], 0, 0)], Length).

% checking strict conditions ------------------------------------------

check_strict_conditions_of_any_two_lessons(event(Group1, Lesson1, RoomID1, Teacher1, Day1, Slot1), event(Group2, Lesson2, RoomID2, Teacher2, Day2, Slot2)) :-
    (
        (
            (
                Group1 == Group2;
                Teacher1 == Teacher2
            ),
            (
                (
                    Day1 #\= Day2;
                    Slot1 #\= Slot2
                )
            )
        );
        (
            (
                \+Group1 == Group2;
                \+Teacher1 == Teacher2
            ),
            (
                (
                    Day1 #\= Day2;
                    Slot1 #\= Slot2;
                    \+RoomID1 == RoomID2
                )
            )
        )
    ).

check_capacity_condition_of_Lesson(event(Group1, LessonID, RoomID, _, _, _)) :-
    group_cap(Group1, Amt),
    classroom_capacity(RoomID, Capacity),
    Amt #> Capacity.

check_the_added_Lesson(_, []).
check_the_added_Lesson(AddedLesson, [Lesson|RestLessons]) :-
    \+check_strict_conditions_of_any_two_lessons(AddedLesson, Lesson),
    check_the_added_Lesson(AddedLesson, RestLessons).

check_neighbor(node([AddedLesson |Schedule], _, _)) :-
    \+check_capacity_condition_of_Lesson(AddedLesson),
    check_the_added_Lesson(AddedLesson, Schedule).

% ------------------------------------------------------------------------

event_req(C0, C1, T) :- 
    =(C0, C1, T).

% getting neighbor--------------------------------------------------------

neighbor(node(State, Length, _), node(NewState, NewLength, NewCost)) :- 
    group_subject_teacher_times(Group, Lesson, Teacher, Times),
    tfilter(=(event(Group, Lesson, Teacher, _, _, _)), State, Vals),
    length(Vals, Length),
    Length #=< Times,
    format("length:~d times:~d", [Length, Times]),
    classroom_available(RoomID, Day, Start, End),
    slots_per_week(SPW),
    slots_per_day(SPD),
    Days #= SPW / SPD,
    between(1, Days, Day),
    LastTimeToStartTheLesson #= End + 1,
    random_integer(Start, LastTimeToStartTheLesson, Slot),
    NewState = [event(Group, Lesson, RoomID, Teacher, Day, Slot)|State],
    NewLength #= Length + 1,
    cost(schedule(NewState), NewCost),
    pretty_print(schedule(NewState)).

% ------------------------------------------------------------------------

ucs_traverse([node(State, Goal, Cost)| _], Goal) :-
    format("\nTotal penalty: ~w\n\n\nSCHEDULE\n", Cost),
    pretty_print(schedule(State)),!.

ucs_traverse([Node | Rest], Goal) :-
    findall(Neighbor, (neighbor(Node, Neighbor), check_neighbor(Neighbor)), Neighbors),
    update(Neighbors, Rest, NewFrontier),
    ucs_traverse(NewFrontier, Goal).


update([], F, F). 
update(NewNodes, Frontier, NewFrontier) :-
    append(NewNodes, Frontier, Nodes),
    quicksort_node(Nodes, NewFrontier).


quicksort_node([], []).
quicksort_node([X | Tail], Sorted):-
    split(X, Tail, Small, Big),
    quicksort_node(Small, SortedSmall),
    quicksort_node(Big, SortedBig),
    concatenate(SortedSmall, [X| SortedBig], Sorted).

split(X, [], [], []).
split(node(_, _, H1), [node(_, _, H2)| Tail], [node(_, _, H2) | Small], Big):-
    H1 > H2, !,
    split(node(_, _, H1), Tail, Small, Big).
split(X, [Y| Tail], Small, [Y | Big]):-
    split(X, Tail, Small, Big).

concatenate([],List,List).
concatenate([Item|List1],List2,[Item|List3]) :-
    concatenate(List1,List2,List3).

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  Подсчет пенальти.
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

cost(Schedule, 0).
%cost(Schedule, Cost) :- cost(Schedule, Cost, _).

schedule_violates_constraint(Schedule, Violated) :- cost(Schedule, _, Violated).

cost(Reqs, Cost, Violated) :-
	findall(G, group_cap(G, _), Groups),
	findall(T, group_subject_teacher_times(_, _, T, _), Teachers),
	prepare_env(Exams),
	teacher_loop(Teacher, Events, Buffer, Violated),
	length(Teachers, Len),
	Cost is Buffer / Len.


teacher_loop(Persons, Events, Cost, Violated) :- person_loop(Persons, Events, 0, [], Cost, Violated).

teacher_loop([], _, Cost, Violated, Cost, Violated).
teacher_loop([P | Ps], Events, Cost0, Violated0, Cost, Violated) :-
	event_loop(P, Events, ECost, EViolated),
	sort(3, @>=, Events, SortedEvents),
	ex_season_ends(LastDay),
	NewCost is Cost0 + ECost,
	!,
	person_loop(Ps, Events, NewCost, NewViolated, Cost, Violated).
teacher_loop([P | Ps], Events, Cost0, Violated0, Cost, Violated) :-
	event_loop(P, Events, ECost, EViolated),
	sort(3, @=<, Events, SortedEvents),
	ex_season_starts(FirstDay),
	NewCost is Cost0 + ECost,
	!,
	person_loop(Ps, Events, NewCost, NewViolated, Cost, Violated).


event_loop(P, Events, Cost, Violated) :- event_loop(P, Events, 0, [], Cost, Violated).

event_loop(_, [], Cost, Violated, Cost, Violated).
event_loop(P, [event(Ex, Room, Day, Time) | Evs], Cost0, Violated0, Cost, Violated) :-
	relates_to(P, Ex),
	no_exam_in_period(P, event(Ex, Room, Day, Time), Cost1, Violated1),
	lunch_break(P, event(Ex, Room, Day, Time), Cost2, Violated2),
	not_in_period(P, event(Ex, Room, Day, Time), Cost3, Violated3),
	same_day(P, event(Ex, Room, Day, Time), Evs, Cost4, Violated4),
	NewCost is Cost0 + Cost1 + Cost2 + Cost3 + Cost4,
	append(Violated1, Violated0, V1),
	append(Violated2, V1, V2),
	append(Violated3, V2, V3),
	append(Violated4, V3, NewViolated),
	!,
	event_loop(P, Evs, NewCost, NewViolated, Cost, Violated).
event_loop(P, [_ | Evs], Cost0, Violated0, Cost, Violated) :-
	event_loop(P, Evs, Cost0, Violated0, Cost, Violated).


no_exam_in_period(P, event(_,_,Day,Start), Penalty, [c_no_exam_in_period(P,Day,From,Till,Penalty)]) :-
	c_no_exam_in_period(P, Day, From, Till, Penalty),
	TillMin is Till - 1,
	between(From,TillMin,Start).
no_exam_in_period(P, event(Ex,_,Day,Start), Penalty, [c_no_exam_in_period(P,Day,From,Till,Penalty)]) :-
	c_no_exam_in_period(P, Day, From, Till, Penalty),
	exam_duration(Ex, Duration),
	End is Start + Duration,
	FromPlus is From + 1,
	between(FromPlus,Till,End).
no_exam_in_period(_,_,0,[]).



lunch_break(P, event(Ex,_,_,Start), Penalty, [c_lunch_break(P,Ex,Penalty)]) :-
	Start == 12,
	c_lunch_break(P,Penalty).
lunch_break(P, event(Ex,_,_,Start), Penalty, [c_lunch_break(P,Ex,Penalty)]) :-
	exam_duration(Ex, Duration),
	End is Start + Duration,
	End == 13,
	c_lunch_break(P, Penalty).
lunch_break(_, _, 0, []).



not_in_period(P, event(Ex,_,Day,Start), Penalty, [c_not_in_period(P,Ex,Day,From,Till,Penalty)]) :-
	c_not_in_period(P,Ex,Day,From,Till,Penalty),
	TillMin is Till - 1,
	between(From,TillMin,Start).
not_in_period(P, event(Ex,_,Day,Start), Penalty, [c_not_in_period(P,Ex,Day,From,Till,Penalty)]) :-
	c_not_in_period(P,Ex,Day,From,Till,Penalty),
	exam_duration(Ex, Duration),
	End is Start + Duration,
	FromPlus is From + 1,
	between(FromPlus,Till,End).
not_in_period(_, _, 0, []).



same_day(P, Event, OtherEvents, Cost, Violated) :-
	same_day(P, Event, OtherEvents, 0, [], Cost, Violated).

same_day(_, _, [], Cost, Violated, Cost, Violated).
same_day(P, event(Ex,Room,Day,Start), [event(Ex2,_,Day,_)|Evs], Cost0, Violates0, Cost, Violated) :-
	relates_to(P, Ex2),
	c_no_exams_same_day(P, Penalty),
	NewCost is Cost0 + Penalty,
	append([c_same_day(P, Ex2, Ex, Penalty)], Violates0, NewViolates),
	!,
	same_day(P, event(Ex,Room,Day,Start), Evs, NewCost, NewViolates, Cost, Violated).
same_day(P, event(Ex,Room,Day,Start), [_|Evs], Cost0, Violates0, Cost, Violated) :-
	same_day(P, event(Ex,Room,Day,Start), Evs, Cost0, Violates0, Cost, Violated).


/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  Вывод расписаний для тестрирования.
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

pretty_print(schedule(Events)) :-
    format("~n~n~n ------------ ~s ---------- ~n", ["Start"]),
	findall(_, print_day(Events), _),
    format("~n ------------ ~s ---------- ~n", ["End"]).


print_day(Events) :-
	setof(event(Group, Lesson, RoomID, Teacher, _, Slot), RoomID^Slot^member(event(Group, Lesson, RoomID, Teacher, Day, Slot),Events), Result),
	format("~n ~n*** DAY ~d *** ~n ~n", [Day]),
	findall(_, print_event(Result), _).


print_event(Events) :-
	setof(event(Group, Lesson, _, Teacher, Day, Slot), Slot^member(event(Group, Lesson, RoomID, Teacher, Day, Slot),Events), Result),
	classroom(RoomID, RoomName),
	format("~s:~n", [RoomName]),
	quicksort_events_by_time(Result, Sorted),
	print(Sorted).


quicksort_events_by_time([], []).
quicksort_events_by_time([X | Tail], Sorted):-
    split2(X, Tail, Small, Big),
    quicksort_events_by_time(Small, SortedSmall),
    quicksort_events_by_time(Big, SortedBig),
    concatenate(SortedSmall, [X| SortedBig], Sorted).

split2(X, [], [], []).
split2(event(_, _, _, _, Day1, Slot1), [event(_, _, _, _, Day2, Slot2)| Tail], [event(_, _, _, _, Day2, Slot2) | Small], Big):-
    (Day1 > Day2;
        (
            Day1 == Day2,
            Slot1 > Slot2
        )
    ),
    !,
    split2(event(_, _, _, _, Day1, Slot1), Tail, Small, Big).
split2(X, [Y| Tail], Small, [Y | Big]):-
    split2(X, Tail, Small, Big).



print([]).

print([event(_, Lesson, _, Teacher, _, Slot)|RestEvents]) :-
	format("~d ", [Slot]),
	format("~s ", [Lesson]),
	format("(~s).~n", [Teacher]),
	print(RestEvents).


/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   Здесь находятся функции для парсинга элементов внутри блока.
   Пример:
        из блока <req subject="ml" teacher="Неделько В.М." amount="2"/>
        достаются subject, teacher, amount.
        amount - числовая переменная, поэтому для неё проводится дополнительное
        преобразование в число.
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

attrs_values(Node, As, Vs) :-
        maplist(attr_value(Node), As, Vs).
 

attr_value(Node, Attr, Value) :-
        (   xpath(Node, /self(@Attr), Value0) -> true
        ;   throw('attribute expected'-Node-Attr)
        ),
        (   numeric_attribute(Attr) -> number_chars(Value, Value0)
        ;   Value = Value0
        ).
        
numeric_attribute(amount).
numeric_attribute(lesson1).
numeric_attribute(lesson2).
numeric_attribute(slot).
numeric_attribute(slotsperweek).
numeric_attribute(slotsperday).
numeric_attribute(lesson).
numeric_attribute(day).

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   Парсинг по блокам XML файла, для каждого типа блока
   есть своя функция парсинга. Например, globals - парсит глобальные 
   константы.
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

elements_([], _) --> [].
elements_([E|Es], Goal) -->
        call(Goal, E),
        elements_(Es, Goal).

process_nodes(What, R, Goal) -->
        { findall(Element, xpath(R, //What, Element), Elements) },
        elements_(Elements, Goal).


process_req(GroupId, Node) -->
        { attrs_values(Node, [subject,teacher,amount], [Subject,Teacher,Amount]) },
        [group_subject_teacher_times(GroupId,Subject,Teacher,Amount)].


process_coupling(GroupId, Node) -->
        { attrs_values(Node, [subject,lesson1,lesson2], [Subject,Slot1,Slot2]) },
        [coupling(GroupId,Subject,Slot1,Slot2)].


process_free(GroupId, Node) -->
        { attrs_values(Node, [slot], [Slot]) },
        [group_freeslot(GroupId,Slot)].


process_group(Node) -->
        { attrs_values(Node, [id], [Id]) },
        process_nodes(req, Node, process_req(Id)),
        process_nodes(coupling, Node, process_coupling(Id)),
        process_nodes(free, Node, process_free(Id)).


globals(Content) -->
        { xpath_chk(Content, //global, Global),
          attrs_values(Global, [slotsperweek, slotsperday], [SPW,SPD]) },
        [slots_per_day(SPD),slots_per_week(SPW)].


process_room(Node) -->
        { attrs_values(Node, [id], [Id]) },
        process_nodes(allocate, Node, process_allocation(Id)).


process_allocation(RoomId, Node) -->
        { attrs_values(Node, [group,subject,lesson], [Group,Subject,Lesson]) },
        [room_alloc(RoomId,Group,Subject,Lesson)].


process_freeday(Node) -->
        { attrs_values(Node, [teacher, day], [Teacher,Day]) },
        [teacher_freeday(Teacher,Day)].

requirementsXML(File) -->
        { load_xml(File, AST, []),
          xpath_chk(AST, //requirements, R) },
        globals(R),
        process_nodes(group, R, process_group),
        process_nodes(room, R, process_room),
        process_nodes(freeday, R, process_freeday).

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  Блок вывода расписаний в XML файл.
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create_timetable :-
    TimeTable = timetable([day(1, 'Monday', [ 
                timeSlot(1, [group('22215', 'Group A')], subject('teamPr', 'Team Project'), teacher('t1', 'Иртегов Д.В.'), room('r1', 'Lecture Hall 1')),
                timeSlot(2, [group('22215', 'Group A')], subject('prog', 'Programming'), teacher('t2', 'Мигинский Д.С.'), room('r2', 'Computer Lab 2')),
                timeSlot(3, [group('33316', 'Group B')], subject('math', 'Mathematics'), teacher('t3', 'Васкевич В.Л.'), room('r3', 'Math Classroom')),
                timeSlot(4, [group('22215', 'Group A'), group('33316', 'Group B')], subject('mobDev', 'Mobile Development'), teacher('t4', 'Букшев И.Е.'), room('r4', 'Lecture Hall 2'))
            ])
    ]),
    
    open('timetable.xml', write, Stream),
    xml_write(Stream, TimeTable),
    close(Stream).


xml_write(Stream, timetable(Days)) :-
    phrase_to_stream(phrase("<timetable>\n"), Stream),
    maplist(write_day(Stream), Days),
    phrase_to_stream(phrase("</timetable>\n"), Stream).


write_day(Stream, day(Number, Name, TimeSlots)) :-
    phrase_to_stream(format_("\t <day number=\"~w\" name=\"~w\">\n", [Number, Name]), Stream),
    write_timeSlots(Stream, TimeSlots),
    phrase_to_stream(phrase("\t </day>\n"), Stream).


write_timeSlots(Stream, TimeSlots) :-
    phrase_to_stream(phrase("\t\t <timeSlots>\n"), Stream),
    maplist(write_timeSlot(Stream), TimeSlots),
    phrase_to_stream(phrase("\t\t </timeSlots>\n"), Stream).


write_timeSlot(Stream, timeSlot(Id, Groups, Subject, Teacher, Room)) :-
    phrase_to_stream(phrase("\t\t\t <timeSlot>\n"), Stream),
    phrase_to_stream(format_("\t\t\t\t <id>~w</id>\n", [Id]), Stream),
    write_groups(Stream, Groups),
    write_subject(Stream, Subject),
    write_teacher(Stream, Teacher),
    write_room(Stream, Room),
    phrase_to_stream(phrase("\t\t\t </timeSlot>\n"), Stream).


write_groups(Stream, Groups) :-
    phrase_to_stream(phrase("\t\t\t\t <groups>\n"), Stream),
    maplist(write_group(Stream), Groups),
    phrase_to_stream(phrase("\t\t\t\t </groups>\n"), Stream).

write_group(Stream, group(Id, Name)) :-
    phrase_to_stream(format_("\t\t\t\t\t <group id=\"~w\" name=\"~w\"/>\n", [Id, Name]), Stream).


write_subject(Stream, subject(Id, Name)) :-
    phrase_to_stream(format_("\t\t\t\t\t <subject id=\"~w\" name=\"~w\"/>\n", [Id, Name]), Stream).


write_teacher(Stream, teacher(Id, Name)) :-
    phrase_to_stream(format_("\t\t\t\t\t <teacher id=\"~w\" name=\"~w\"/>\n", [Id, Name]), Stream).


write_room(Stream, room(Id, Name)) :-
    phrase_to_stream(format_("\t\t\t\t\t <room id=\"~w\" name=\"~w\"/>\n", [Id, Name]), Stream).
