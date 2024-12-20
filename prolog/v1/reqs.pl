:- discontiguous(group_subject_teacher_times/4).
:- discontiguous(group_freeslot/2).

% количество доступных слотов на неделе
slots_per_week(42).
slots_per_day(7).

sp_unit_profile(1, "Пермяков Р.А.", teacher).
sp_unit_profile(2, "Букшев И.Е.", teacher).
sp_unit_profile(3, "Неделько В.М.", teacher).
sp_unit_profile(4, "Васкевич В.Л.", teacher).
sp_unit_profile(5, "Куталев А. В.", teacher).
sp_unit_profile(6, "Иртегов Д.В.", teacher).
sp_unit_profile(7, "Мигинский Д.С.", teacher).
sp_unit_profile(8, "Васкевич В.Л.", teacher).

faculties(1, "FIT").

faculty_group(1, '22215').

group_student_amount('22215', 8).

classroom(100, "Small room").
classroom(200, "Large room").


classroom_capacity(100, 50).
classroom_capacity(200, 50).

% Доступность комнат
classroom_available(Room, 1, 1, 7) :- classroom(Room, _). % Day 1, all rooms are available from 9 to 18
classroom_available(Room, 2, 1, 7) :- classroom(Room, _). % Day 2, all rooms are available from 9 to 18
classroom_available(Room, 3, 1, 7) :- classroom(Room, _). % Day 3, all rooms are available from 9 to 18
classroom_available(Room, 4, 1, 7) :- classroom(Room, _). % Day 4, all rooms are available from 9 to 18
classroom_available(Room, 5, 1, 7) :- classroom(Room, _). % Day 5, all rooms are available from 9 to 18
classroom_available(Room, 6, 1, 7) :- classroom(Room, _). % Day 6, all rooms are available from 9 to 18

group('22215').
% Назначение группам предметов и преподавателей
group_subject_teacher_times('22215', ks, "Пермяков Р.А.", 2).
group_subject_teacher_times('22215', mobDev, "Букшев И.Е.", 2).
group_subject_teacher_times('22215', ml, "Неделько В.М.", 2).
group_subject_teacher_times('22215', fiz, "Тишкевич В.И.", 2).
group_subject_teacher_times('22215', dpas, "Куталев А. В.", 2).
group_subject_teacher_times('22215', teamPr, "Иртегов Д.В.", 2).
group_subject_teacher_times('22215', prog, "Мигинский Д.С.", 2).
group_subject_teacher_times('22215', math, "Васкевич В.Л.", 2).
group_freeslot('22215', 1).

% coupling('22215', teamPr, 0, 1).
room_alloc('304', '22215', teamPr, 1).
% room_alloc('304', '22214', teamPr, 1).
% room_alloc('304', '22213', teamPr, 1).

% group_subject_teacher_times('22214', ks, "Пермяков Р.А.", 2).
% group_subject_teacher_times('22214', mobDev, "Букшев И.Е.", 2).
% group_subject_teacher_times('22214', ml, "Неделько В.М.", 2).
% group_subject_teacher_times('22214', fiz, "Васкевич В.Л.", 2).
% group_subject_teacher_times('22214', dpas, "Куталев А. В.", 2).
% group_subject_teacher_times('22214', teamPr, "Иртегов Д.В.", 2).
% group_subject_teacher_times('22214', prog, "Мигинский Д.С.", 2).
% group_subject_teacher_times('22214', math, "Васкевич В.Л.", 2).

% group_subject_teacher_times('22213', ks, "Пермяков Р.А.", 2).
% group_subject_teacher_times('22213', mobDev, "Букшев И.Е.", 2).
% group_subject_teacher_times('22213', ml, "Неделько В.М.", 2).
% group_subject_teacher_times('22213', fiz, "Васкевич В.Л.", 2).
% group_subject_teacher_times('22213', dpas, "Куталев А. В.", 2).
% group_subject_teacher_times('22213', teamPr, "Иртегов Д.В.", 2).
% group_subject_teacher_times('22213', prog, "Мигинский Д.С.", 2).
% group_subject_teacher_times('22213', math, "Васкевич В.Л.", 2).

% свободные дни преподавателей
teacher_freeday("Иртегов Д.В.", 0).
teacher_freeday("Иртегов Д.В.", 1).
teacher_freeday("Иртегов Д.В.", 2).
