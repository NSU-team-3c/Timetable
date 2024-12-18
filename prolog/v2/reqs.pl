

slots_per_week(42).
slots_per_day(7).


% Groups
group_cap("22215", 12).

% Subject
group_subject_teacher_times("22215", "math", "Irtegov", 2).
group_subject_teacher_times("22215", "kb", "Permeckov", 2).


% Room capacity
classroom_capacity("3107", 10).
classroom_capacity("2128", 12).

% Availability of rooms
classroom_available(Room, 1, 1, 7) :- classroom_capacity(Room, _). % Day 1, all rooms are available from 10 to 12
classroom_available(Room, 2, 1, 7) :- classroom_capacity(Room, _). % Day 2, all rooms are available from 10 to 12
% classroom_available(Room, 3, 1, 7) :- classroom(Room). % Day 3, all rooms are available from 10 to 15
% classroom_available(Room, 4, 1, 7) :- classroom(Room). % Day 4, all rooms are available from 10 to 12
% classroom_available(Room, 5, 1, 7) :- classroom(Room). % Day 5, all rooms are available from 10 to 12


% Teachers preferences
teacher_lunch_break(Teacher, 1) :- teacher(Teacher).

% Irtegov available in any day
teacher_available("Irtegov", 1, 1, 5).
teacher_available("Permeckov", 1, 1, 5).
teacher_available("Irtegov", 2, 1, 5).
teacher_available("Permeckov", 2, 1, 5).

subject_not_in_day("math", 1, 3).


% Students preferences

% All students prefer a lunch break
group_lunch_break(Group, 1) :- group_cap(Group, _).

% Teacher-class correspondence table
teaches("Irtegov", "math").


% Group-subject correspondence table
follows('22215', "math").
