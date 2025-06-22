import { uniqueId } from 'lodash';

interface MenuitemsType {
  [x: string]: any;
  id?: string;
  navlabel?: boolean;
  subheader?: string;
  title?: string;
  icon?: any;
  href?: string;
  children?: MenuitemsType[];
  chip?: string;
  chipColor?: string;
  variant?: string;
  external?: boolean;
}
import {
  IconAward,
  IconBoxMultiple,
  IconPoint,
  IconAlertCircle,
  IconNotes,
  IconCalendar,
  IconMail,
  IconTicket,
  IconEdit,
  IconGitMerge,
  IconCurrencyDollar,
  IconApps,
  IconFileDescription,
  IconFileDots,
  IconFiles,
  IconBan,
  IconStar,
  IconMoodSmile,
  IconBorderAll,
  IconBorderHorizontal,
  IconBorderInner,
  IconBorderVertical,
  IconBorderTop,
  IconUserCircle,
  IconPackage,
  IconMessage2,
  IconBasket,
  IconChartLine,
  IconChartArcs,
  IconChartCandle,
  IconChartArea,
  IconChartDots,
  IconChartDonut3,
  IconChartRadar,
  IconLogin,
  IconUserPlus,
  IconRotate,
  IconBox,
  IconShoppingCart,
  IconAperture,
  IconLayout,
  IconSettings,
  IconHelp,
  IconZoomCode,
  IconBoxAlignBottom,
  IconBoxAlignLeft,
  IconBorderStyle2,
  IconAppWindow
} from '@tabler/icons-react';

export const AdminMenuitems: MenuitemsType[] = [
  {
    navlabel: true,
    subheader: 'Главное',
  },

  {
    id: uniqueId(),
    title: 'Личный кабинет',
    icon: IconAperture,
    href: "/profile",
    chipColor: 'secondary',
  },
   {
    id: uniqueId(),
    title: 'Настройки расписания',
    icon: IconSettings,
    href: "/admin/timetable",
  },
  {
    id: uniqueId(),
    title: 'Обновления в данных',
    icon: IconSettings,
    href: "/admin/timetable/updates",
  }
];

export const TeacherMenuitems: MenuitemsType[] = [
  {
    navlabel: true,
    subheader: 'Главное',
  },

  {
    id: uniqueId(),
    title: 'Личный кабинет',
    icon: IconAperture,
    href: "/profile",
    chipColor: 'secondary',
  },
   {
    id: uniqueId(),
    title: 'Свободное время',
    icon: IconSettings,
    href: "/admin/timetable",
  },
  {
    id: uniqueId(),
    title: 'Моё расписание',
    icon: IconSettings,
    href: "/profile/timetable",
  },
  {
    id: uniqueId(),
    title: 'Обновления',
    icon: IconSettings,
    href: "/profile/notifications",
  },
  {
    navlabel: true,
    subheader: 'Дополнительно',
  },
  {
    id: uniqueId(),
    title: 'Поддержка',
    icon: IconHelp,
    chipColor: 'secondary',
    href: "/profile/faq",
  },
  {
    id: uniqueId(),
    title: 'О нас',
    icon: IconPackage,
    chipColor: 'secondary',
    href: "/profile/about",
  }
];

export const StudentMenuitems: MenuitemsType[] = [
  {
    navlabel: true,
    subheader: 'Главное',
  },

  {
    id: uniqueId(),
    title: 'Личный кабинет',
    icon: IconAperture,
    href: "/profile",
    chipColor: 'secondary',
  },
   {
    id: uniqueId(),
    title: 'Мое расписание',
    icon: IconSettings,
    href: "/profile/timetable",
  },
  {
    id: uniqueId(),
    title: 'Обновления',
    icon: IconSettings,
    href: "/profile/notifications",
  },
  {
    navlabel: true,
    subheader: 'Дополнительно',
  },
  {
    id: uniqueId(),
    title: 'Поддержка',
    icon: IconHelp,
    chipColor: 'secondary',
    href: "/profile/faq",
  },
  {
    id: uniqueId(),
    title: 'О нас',
    icon: IconPackage,
    chipColor: 'secondary',
    href: "/profile/about",
  }
];