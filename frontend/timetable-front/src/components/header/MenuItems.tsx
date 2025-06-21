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

const Menuitems: MenuitemsType[] = [
    {
        id: uniqueId(),
        title: 'Личный кабинет',
        href: '/profile',
        chipColor: 'secondary',
    },
    {
        id: uniqueId(),
        title: 'О нас',
        href: '/auth/about',
        chipColor: 'secondary',
    },
    {
        id: uniqueId(),
        title: 'Поддержка',
        href: '/auth/faq',
        chipColor: 'secondary',
    },
];

export default Menuitems;
