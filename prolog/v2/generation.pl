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

:- dynamic(group_subject_teacher_times/4).
:- dynamic(classroom_available/4).
:- dynamic(teacher_available/4).
:- dynamic(classroom_capacity/2).
:- dynamic(classroom/2).
:- dynamic(group_cap/2).
:- dynamic(slots_per_day/1).
:- dynamic(slots_per_week/1).

:- discontiguous(group_subject_teacher_times/4).

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
check_strict_conditions_of_any_two_lessons(event(Group1, Lesson1, RoomID1, Teacher1, Day1, Slot1), event(Group1, Lesson1, RoomID1, Teacher1, Day1, Slot1)) :- false.
check_strict_conditions_of_any_two_lessons(event(Group1, _, RoomID1, Teacher1, Day1, Slot1), event(Group2, _, RoomID2, Teacher2, Day2, Slot2)) :-
    (
    (
            (
                Group1 == Group2
            ),
            (
                (
                    Day1 #\= Day2;
                    Slot1 #\= Slot2
                )
            )
    );
    (
        Group1 \= Group2,
        (
            RoomID1 \= RoomID2;
            (
                Day1 #\= Day2;
                Slot1 #\= Slot2
            )
        )
    )
    ),
    (
    (
        (
            Teacher1 == Teacher2
        ),
        (
            (
                Day1 #\= Day2;
                Slot1 #\= Slot2
            )
        )
    );
    Teacher1 \= Teacher2).

check_capacity_condition_of_lesson(event(Group1, LessonID, RoomID, _, _, _)) :-
    group_cap(Group1, Amt),
    classroom_capacity(RoomID, Capacity),
    Amt #> Capacity.

check_teacher_availability(event(_, _, _, Teacher, Day, Slot)) :-
    teacher_available(Teacher, Day, Start, End),
    between(Start, End, Slot).

check_the_added_lesson(_, []).
check_the_added_lesson(AddedLesson, [Lesson|RestLessons]) :-
    check_strict_conditions_of_any_two_lessons(AddedLesson, Lesson),
    check_the_added_lesson(AddedLesson, RestLessons).

check_neighbor(node([AddedLesson |Schedule], _, _)) :-
    \+check_capacity_condition_of_lesson(AddedLesson),
    check_teacher_availability(AddedLesson),
    check_the_added_lesson(AddedLesson, Schedule).

% ------------------------------------------------------------------------

event_req(C0, C1, T) :- 
    =(C0, C1, T).

count([],X,0).
count([X|T],X,Y):- count(T,X,Z), Y #= 1+Z.
count([X1|T],X,Z):- X1\=X,count(T,X,Z).

% getting neighbor--------------------------------------------------------

neighbor(node(State, Length, _), node(NewState, NewLength, 0)) :- 
    group_subject_teacher_times(Group, Lesson, Teacher, Times),
    findall(event(Group, Lesson, _, Teacher, _, _), member(event(Group, Lesson, _, Teacher, _, _),State), Result),
    length(Result, Val),
    Val #< Times,
    slots_per_week(SPW),
    slots_per_day(SPD),
    Days #= SPW / SPD + 1,
    between(1, Days, Day),
    classroom_available(RoomID, Day, Start, End),
    LastTimeToStartTheLesson #= End + 1,
    between(Start, LastTimeToStartTheLesson, Slot),
    
    % write('Slot: '), write(Slot), nl,     
    % write('Group: '), write(Group), nl,    
    % write('Teacher: '), write(Teacher), nl,
    % write('Room: '), write(RoomID), nl,     
    % write('Lesson: '), write(Lesson), nl,  
    % write('Day: '), write(Day), nl,  
    % write('State:'), write(State), nl,
    %between(1, SPD, Slot),
    NewState = [event(Group, Lesson, RoomID, Teacher, Day, Slot)|State],
    NewLength #= Length + 1.
    % cost(schedule(NewState), NewCost).

% ------------------------------------------------------------------------

ucs_traverse([node(State, Goal, Cost)| _], Goal) :-
    format("\nTotal penalty: ~d\n\n\nSCHEDULE\n", [Cost]),
    write('State:'), write(State), nl,
    pretty_print(schedule(State)), create_timetable(schedule(State)),!.

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

cost(Schedule, Cost) :- cost(Schedule, Cost, _).

schedule_violates_constraint(Schedule, Violated) :- cost(Schedule, _, Violated).

cost(Schedule, Cost, Violated) :-
	findall(G, group_cap(G, _), Groups),
	findall(T, group_subject_teacher_times(_, _, T, _), Teachers),
	teacher_loop(Teacher, Events, Buffer, Violated),
	length(Teachers, Len),
	Cost #= Buffer / Len.


teacher_loop(Persons, Events, Cost, Violated) :- person_loop(Persons, Events, 0, [], Cost, Violated).

teacher_loop([], _, Cost, Violated, Cost, Violated).
teacher_loop([P | Ps], Events, Cost0, Violated0, Cost, Violated) :-
	event_loop(P, Events, ECost, EViolated),
	sort(3, @>=, Events, SortedEvents),
	ex_season_ends(LastDay),
	NewCost is Cost0 + ECost,
	!,
	person_loop(Ps, Events, NewCost, NewViolated, Cost, Violated).


event_loop(P, Events, Cost, Violated) :- event_loop(P, Events, 0, [], Cost, Violated).

event_loop(_, [], Cost, Violated, Cost, Violated).
event_loop(Teacher, [event(Group, Lesson, RoomID, Teacher, Day, Slot) | Evs], Cost0, Violated0, Cost, Violated) :-
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



lunch_break(P, event(Group, Lesson, RoomID, Teacher, Day, Slot), Penalty, [c_lunch_break(P,Ex,Penalty)]) :-
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
	format("~s:~n", [RoomID]),
	quicksort_events_by_time(Result, Sorted),
	print(Sorted).


quicksort_events_by_time([], []).
quicksort_events_by_time([X | Tail], Sorted):-
    split2(X, Tail, Small, Big),
    quicksort_events_by_time(Small, SortedSmall),
    quicksort_events_by_time(Big, SortedBig),
    concatenate(SortedSmall, [X| SortedBig], Sorted).

split2(X, [], [], []).
split2(event(Group1, Lesson1, RoomID1, Teacher1, Day1, Slot1), [event(Group2, Lesson2, RoomID2, Teacher2, Day2, Slot2)| Tail], [event(Group2, Lesson2, RoomID2, Teacher2, Day2, Slot2) | Small], Big):-
    (Day1 #> Day2;
        (
            Day1 == Day2,
            Slot1 #> Slot2
        )
    ),
    !,
    split2(event(Group1, Lesson1, RoomID1, Teacher1, Day1, Slot1), Tail, Small, Big).
split2(X, [Y| Tail], Small, [Y | Big]):-
    split2(X, Tail, Small, Big).



print([]).

print([event(Group, Lesson, _, Teacher, _, Slot)|RestEvents]) :-
	format("\tSlot: ~d | ", [Slot]),
    format("Group: ~s | ", [Group]),
	format("Lesson: ~s | ", [Lesson]),
	format("Teacher: ~s~n", [Teacher]),
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
numeric_attribute(capacity).
numeric_attribute(lesson2).
numeric_attribute(slot).
numeric_attribute(slotsperweek).
numeric_attribute(slotsperday).
numeric_attribute(lesson).
numeric_attribute(day).
numeric_attribute(start).
numeric_attribute(end).

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
        { attrs_values(Node, [id, amount], [Id, Amount]) },
        [group_cap(Id, Amount)],
        process_nodes(req, Node, process_req(Id)),
        process_nodes(coupling, Node, process_coupling(Id)),
        process_nodes(free, Node, process_free(Id)).


globals(Content) -->
        { xpath_chk(Content, //global, Global),
          attrs_values(Global, [slotsperweek, slotsperday], [SPW,SPD]) },
        [slots_per_day(SPD),slots_per_week(SPW)].

process_room(Node) -->
        { attrs_values(Node, [id, capacity], [Id, Capacity]) },
        [classroom_capacity(Id, Capacity)],
        process_nodes(allocate, Node, process_allocation(Id)).

process_teacher(Node) -->
        { attrs_values(Node, [teacher], [Teacher]) },
        process_nodes(slot, Node, teacher_timeslot_allocation(Teacher)).

teacher_timeslot_allocation(Teacher, Node) -->
        { attrs_values(Node, [day, start, end], [Day, Start, End]) },
        [teacher_available(Teacher,Day,Start,End)].


process_allocation(RoomId, Node) -->
        { attrs_values(Node, [day, start, end], [Day, Start, End]) },
        [classroom_available(RoomId,Day,Start,End)].


process_freeday(Node) -->
        { attrs_values(Node, [teacher, day], [Teacher,Day]) },
        [teacher_freeday(Teacher,Day)].

requirementsXML(File) -->
        { load_xml(File, AST, []),
          xpath_chk(AST, //requirements, R) },
        globals(R),
        process_nodes(group, R, process_group),
        process_nodes(room, R, process_room),
        process_nodes(freeday, R, process_freeday),
        process_nodes(availableTeacherSlots, R, process_teacher).

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  Блок вывода расписаний в XML файл.
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

create_timetable(Schedule) :-
    open('timetable.xml', write, Stream),
    xml_write(Stream, Schedule),
    close(Stream).

xml_write(Stream, schedule(Events)) :-
    phrase_to_stream(phrase("<timetable>\n"), Stream),
    findall(_, write_day(Stream, Events), _),
    phrase_to_stream(phrase("</timetable>\n"), Stream).


write_day(Stream, Events) :-
    setof(event(Group, Lesson, RoomID, Teacher, _, Slot), RoomID^Slot^member(event(Group, Lesson, RoomID, Teacher, Day, Slot),Events), Result),
    phrase_to_stream(format_("\t <day number=\"~w\">\n", [Day]), Stream),
    write_event(Stream, Result),
    phrase_to_stream(phrase("\t </day>\n"), Stream).

write_event(Stream, []).
write_event(Stream, [event(Group, Lesson, RoomID, Teacher, Day, Slot)|Rest]) :-
    phrase_to_stream(phrase("\t\t <timeSlots>\n"), Stream),
    phrase_to_stream(format_("\t\t\t\t <id>~d</id>\n", [Slot]), Stream),
    phrase_to_stream(format_("\t\t\t\t\t <group id=\"~s\"/>\n", [Group]), Stream),
    phrase_to_stream(format_("\t\t\t\t\t <subject id=\"~s\"/>\n", [Lesson]), Stream),
    phrase_to_stream(format_("\t\t\t\t\t <teacher id=\"~s\"/>\n", [Teacher]), Stream),
    phrase_to_stream(format_("\t\t\t\t\t <room id=\"~s\"/>\n", [RoomID]), Stream),
    phrase_to_stream(phrase("\t\t </timeSlots>\n"), Stream),
    write_event(Stream, Rest).


/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   Входная точка прораммы, на вход подается имя файла с требованиями.
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

timetable(FileName) :-
        phrase(requirementsXML(file(FileName)), Reqs),
        setup_call_cleanup(
                maplist(assertz, Reqs),
                (   
                        main
                ;   
                        false
                ),
                maplist(retract, Reqs)
        ).


run :- timetable("reqs.xml").