:- discontiguous(group_subject_teacher_times/4).
:- discontiguous(group_freeslot/2).

% количество доступных слотов на неделе
slots_per_week(42).
slots_per_day(7).

% максимальное количество пар в день
% max_pairs_a_day('22215', 4).

% Назначение группам предметов и преподавателей
group_subject_teacher_times('22215', ks, "Пермяков Р.А.", 2).
group_subject_teacher_times('22215', mobDev, "Букшев И.Е.", 2).
group_subject_teacher_times('22215', ml, "Неделько В.М.", 2).
group_subject_teacher_times('22215', fiz, "Васкевич В.Л.", 2).
group_subject_teacher_times('22215', dpas, "Куталев А. В.", 2).
group_subject_teacher_times('22215', teamPr, "Иртегов Д.В.", 2).
group_subject_teacher_times('22215', prog, "Мигинский Д.С.", 2).
group_subject_teacher_times('22215', math, "Васкевич В.Л.", 2).
group_freeslot('22215', 1).

coupling('22215', teamPr, 0, 1).
room_alloc('304', '22215', teamPr, 1).
room_alloc('304', '22214', teamPr, 1).
room_alloc('304', '22213', teamPr, 1).

group_subject_teacher_times('22214', ks, "Пермяков Р.А.", 2).
group_subject_teacher_times('22214', mobDev, "Букшев И.Е.", 2).
group_subject_teacher_times('22214', ml, "Неделько В.М.", 2).
group_subject_teacher_times('22214', fiz, "Васкевич В.Л.", 2).
group_subject_teacher_times('22214', dpas, "Куталев А. В.", 2).
group_subject_teacher_times('22214', teamPr, "Иртегов Д.В.", 2).
group_subject_teacher_times('22214', prog, "Мигинский Д.С.", 2).
group_subject_teacher_times('22214', math, "Васкевич В.Л.", 2).

group_subject_teacher_times('22213', ks, "Пермяков Р.А.", 2).
group_subject_teacher_times('22213', mobDev, "Букшев И.Е.", 2).
group_subject_teacher_times('22213', ml, "Неделько В.М.", 2).
group_subject_teacher_times('22213', fiz, "Васкевич В.Л.", 2).
group_subject_teacher_times('22213', dpas, "Куталев А. В.", 2).
group_subject_teacher_times('22213', teamPr, "Иртегов Д.В.", 2).
group_subject_teacher_times('22213', prog, "Мигинский Д.С.", 2).
group_subject_teacher_times('22213', math, "Васкевич В.Л.", 2).

% свободные дни преподавателей
% teacher_freeday("Иртегов Д.В.", 4).