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


:- dynamic(group_subject_teacher_times/4).
:- dynamic(coupling/4).
:- dynamic(teacher_freeday/2).
:- dynamic(slots_per_day/1).
:- dynamic(slots_per_week/1).
:- dynamic(group_freeslot/2).
:- dynamic(room_alloc/4).

:- discontiguous(group_subject_teacher_times/4).
:- discontiguous(group_freeslot/2).

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   Получение данных из фактов пролога.
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

requirements(Rs) :-
        Goal = group_subject_teacher_times(Group,Subject,Teacher,Number),
        setof(req(Group,Subject,Teacher,Number), Goal, Rs0),
        maplist(req_with_slots, Rs0, Rs).

req_with_slots(R, R-Slots) :- R = req(_,_,_,N), length(Slots, N).

% упорядоченное множество групп
groups(Groups) :-
        setof(C, S^N^T^group_subject_teacher_times(C,S,T,N), Groups).

% упорядоченное множество преподавателей
teachers(Teachers) :-
        setof(T, C^S^N^group_subject_teacher_times(C,S,T,N), Teachers).

% упорядоченное множество комнат
rooms(Rooms) :-
        findall(r(Room, C, S, Slot), room_alloc(Room,C,S,Slot), Rooms0),
        sort(Rooms0, Rooms).

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   Основной блок, здесь формируются все требования к расписаниям.
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

requirements_variables(Rs, Vars, Rooms) :-
        requirements(Rs),
        pairs_slots(Rs, Vars),
        slots_per_week(SPW),
        Max #= SPW - 1,
        Vars ins 0..Max,
        maplist(constrain_subject, Rs),
        groups(Groups),
        teachers(Teachers),
        rooms(Rooms),
        maplist(constrain_teacher(Rs), Teachers),
        maplist(constrain_group(Rs), Groups),
        maplist(constrain_room(Rs), Rooms).

% в каком дне недели располагается слот
slot_day(S, Q) :-
        slots_per_day(SPD),
        Q #= S // SPD.


list_without_nths(Es0, Ws, Es) :-
        phrase(without_(Ws, 0, Es0), Es).

without_([], _, Es) --> seq(Es). 
without_([W|Ws], Pos0, [E|Es]) -->
        { Pos #= Pos0 + 1,
          zcompare(R, W, Pos0) },
        without_at_pos0(R, E, [W|Ws], Ws1),
        without_(Ws1, Pos, Es).

without_at_pos0(=, _, [_|Ws], Ws) --> [].
without_at_pos0(>, E, Ws0, Ws0) --> [E].

% совместные пары (пары, которые идут подряд в расписании)
slots_couplings(Slots, F-S) :-
        nth0(F, Slots, S1),
        nth0(S, Slots, S2),
        S2 #= S1 + 1,
        slot_day(S1, Q),
        slot_day(S2, Q).

% прикрепление пары к слоту
constrain_subject(req(Group,Subj,_Teacher,_Num)-Slots) :-
        strictly_ascending(Slots), % break symmetry
        maplist(slot_day, Slots, Qs0),
        findall(F-S, coupling(Group,Subj,F,S), Cs),
        maplist(slots_couplings(Slots), Cs),
        pairs_values(Cs, Seconds0),
        sort(Seconds0, Seconds),
        list_without_nths(Qs0, Seconds, Qs),
        strictly_ascending(Qs).


all_diff_from(Vs, F) :- maplist(#\=(F), Vs).

constrain_group(Rs, Group) :-
        tfilter(group_req(Group), Rs, Sub),
        pairs_slots(Sub, Vs),
        all_different(Vs),
        findall(S, group_freeslot(Group,S), Frees),
        maplist(all_diff_from(Vs), Frees).


constrain_teacher(Rs, Teacher) :-
        tfilter(teacher_req(Teacher), Rs, Sub),
        pairs_slots(Sub, Vs),
        all_different(Vs),
        findall(F, teacher_freeday(Teacher, F), Fs),
        maplist(slot_day, Vs, Qs),
        maplist(all_diff_from(Qs), Fs).

sameroom_var(Reqs, r(Group,Subject,Lesson), Var) :-
        memberchk(req(Group,Subject,_Teacher,_Num)-Slots, Reqs),
        nth0(Lesson, Slots, Var).

constrain_room(Reqs, r(Room, _, _, _)) :- 
        findall(r(Group,Subj,Less), room_alloc(Room,Group,Subj,Less), RReqs),
        maplist(sameroom_var(Reqs), RReqs, Roomvars),
        all_different(Roomvars).


strictly_ascending(Ls) :- chain(#<, Ls).

group_req(C0, req(C1,_S,_T,_N)-_, T) :- =(C0, C1, T).

teacher_req(T0, req(_C,_S,T1,_N)-_, T) :- =(T0,T1,T).

pairs_slots(Ps, Vs) :-
        pairs_values(Ps, Vs0),
        append(Vs0, Vs).

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   Формирование данных в определенный формат для дальнейшего вывода.
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

days_variables(Days, Vs) :-
        slots_per_week(SPW),
        slots_per_day(SPD),
        NumDays #= SPW // SPD,
        length(Days, NumDays),
        length(Day, SPD),
        maplist(same_length(Day), Days),
        append(Days, Vs).

group_days(Rs, Group, Days) :-
        days_variables(Days, Vs),
        tfilter(group_req(Group), Rs, Sub),
        foldl(v(Sub), Vs, 0, _).

v(Rs, V, N0, N) :-
        (   member(req(_,Subject,_,_)-Times, Rs),
            member(N0, Times) -> V = subject(Subject)
        ;   V = free
        ),
        N #= N0 + 1.

group_days_rooms(Rs, Rooms, Group, Days) :-
        days_variables(Days, Vs),
        tfilter(group_req(Group), Rs, Sub),
        foldl(v_rooms(Sub, Rooms), Vs, 0, _).

v_rooms(Rs, Rooms, V, N0, N) :-
        (   member(req(Group,Subject,_,_)-Times, Rs),
            member(r(Room, Group, Subject,_), Rooms),
            member(N0, Times) -> V = subject_room(Subject, Room)
        ;
            member(req(Group,Subject,_,_)-Times, Rs),
            member(N0, Times) -> V = subject(Subject)
        ;   V = free
        ),
        N #= N0 + 1.

teacher_days(Rs, Teacher, Days) :-
        days_variables(Days, Vs),
        tfilter(teacher_req(Teacher), Rs, Sub),
        foldl(v_teacher(Sub), Vs, 0, _).

v_teacher(Rs, V, N0, N) :-
        (   member(req(C,Subj,_,_)-Times, Rs),
            member(N0, Times) -> V = group_subject(C, Subj)
        ;   V = free
        ),
        N #= N0 + 1.

teacher_rooms_days(Rs, Teacher, Days) :-
        days_variables(Days, Vs),
        tfilter(teacher_req(Teacher), Rs, Sub),
        foldl(v_teacher(Sub), Vs, 0, _).

v_teacher_rooms(Rs, _, V, N0, N) :-
        (   member(req(C,Subj,_,_)-Times, Rs),
            member(N0, Times) -> V = group_subject(C, Subj)
        ;   V = free
        ),
        N #= N0 + 1.

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   Отображение объектов 
   Здесь находится блока для отладочного вывода расписаний.

   Можно вывести расписание по группам (print_groups), 
   отдельное расписание для каждого учителя (print_teachers).
   А также можно добавить к этим расписаниям вывод комнат.
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

% Отрисовать расписания всех групп
print_groups(Rs) :-
        groups(Cs),
        phrase_to_stream(format_groups(Cs, Rs), user_output).

print_groups(Rs, Rooms) :-
        groups(Cs),
        phrase_to_stream(format_groups_with_rooms(Cs, Rs, Rooms), user_output).

% Форматирование вывода для каждой группы
format_groups([], _) --> [].
format_groups([Group|Groups], Rs) -->
        { group_days(Rs, Group, Days0),
          transpose(Days0, Days) },
        format_("Группа: ~w~2n", [Group]),
        weekdays_header,
        align_rows(Days),
        format_groups(Groups, Rs).

format_groups_with_rooms([], _, _) --> [].
format_groups_with_rooms([Group|Groups], Rs, Rooms) -->
        { group_days_rooms(Rs,Rooms, Group, Days0),
          transpose(Days0, Days) },
        format_("Группа: ~w~2n", [Group]),
        weekdays_header,
        align_rows(Days),
        format_groups(Groups, Rs).

align_rows([]) --> "\n\n\n".
align_rows([R|Rs]) -->
        align_row(R),
        "\n",
        align_rows(Rs).

align_row([]) --> [].
align_row([R|Rs]) -->
        align_(R),
        align_row(Rs).

align_(free)               --> align_(verbatim('')).
align_(group_subject(C,S)) --> align_(verbatim(C/S)).
align_(subject_room(C,R)) --> align_(verbatim(C/R)).
align_(subject(S))         --> align_(verbatim(S)).
align_(verbatim(Element))  --> format_("~t~w~t~15+", [Element]).

% Вывод расписаний преподавателей
print_teachers(Rs) :-
        teachers(Ts),
        phrase_to_stream(format_teachers(Ts, Rs), user_output).

% Форматирование вывода
format_teachers([], _) --> [].
format_teachers([T|Ts], Rs) -->
        { teacher_days(Rs, T, Days0),
          transpose(Days0, Days) },
        format_("Преподаватель: ~s ~2n", [T]),
        weekdays_header,
        align_rows(Days),
        format_teachers(Ts, Rs).

% Форматирование дней недели
weekdays_header -->
        { maplist(with_verbatim,
                  ['Пн','Вт','Ср','Чт','Пт','Сб'],
                  Vs) },
        align_row(Vs),
        format_("~n~`=t~90|~n", []).

with_verbatim(T, verbatim(T)).


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
   Входная точка прораммы, на вход подается имя файла с требованиями.
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

timetable_(FileName, Rs, Vs, Rooms) :-
        phrase(requirementsXML(file(FileName)), Reqs),
        setup_call_cleanup(
                maplist(assertz, Reqs),
                (   
                        requirements_variables(Rs, Vs, Rooms)
                ;   
                        false
                ),
                maplist(retract, Reqs)
        ).

timetable(FileName) :-
        timetable_(FileName, Rs, Vs, Rooms),
        labeling([ff], Vs),
        print_groups(Rs, Rooms),
        nl, nl,
        print_teachers(Rs),
        nl.


run :- timetable("reqs.xml").


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

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  Формирование расписания для вывода.
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

% Отрисовать расписания всех групп
form_groups(Rs) :-
        groups(Cs),
        phrase_to_stream(format_groups(Cs, Rs), user_output).

print_groups(Rs, Rooms) :-
        groups(Cs),
        phrase_to_stream(format_groups_with_rooms(Cs, Rs, Rooms), user_output).

% Форматирование вывода для каждой группы
format_groups([], _) --> [].
format_groups([Group|Groups], Rs) -->
        { group_days(Rs, Group, Days0),
          transpose(Days0, Days) },
        format_("Группа: ~w~2n", [Group]),
        weekdays_header,
        align_rows(Days),
        format_groups(Groups, Rs).

format_groups_with_rooms([], _, _) --> [].
format_groups_with_rooms([Group|Groups], Rs, Rooms) -->
        { group_days_rooms(Rs,Rooms, Group, Days0),
          transpose(Days0, Days) },
        format_("Группа: ~w~2n", [Group]),
        weekdays_header,
        align_rows(Days),
        format_groups(Groups, Rs).