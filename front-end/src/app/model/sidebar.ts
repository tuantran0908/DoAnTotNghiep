export interface SideBarGroup {
  title: string;
  isExpanded: boolean;
  sideBarItems: SideBarItem[];
  icon?: string;
}
export interface SideBarItem {
  id: string;
  title: string;
  url: string;
  isSelected: boolean;
  type?: number;
  icon?: string;
}
export const SideBarCmsGroups: SideBarGroup[] = [
  {
    title: 'Thống kê, báo cáo',
    isExpanded: false,
    icon: 'pie_chart',
    sideBarItems: [
      {
        id: 'report',
        title: 'Thống kê, báo cáo',
        url: '/cms/report',
        isSelected: false,
      },
    ],
  },
  {
    title: 'Quản trị người dùng',
    isExpanded: false,
    icon: 'manage_accounts',
    sideBarItems: [
      {
        id: 'nguoi-dung',
        title: 'Quản trị người dùng',
        url: '/cms/users',
        isSelected: false,
      },
    ],
  },
  {
    title: 'Quản lý nhóm quyền',
    isExpanded: false,
    icon: 'groups',
    sideBarItems: [
      {
        id: 'role-group',
        title: 'Quản lý nhóm quyền',
        url: '/cms/role-group',
        isSelected: false,
      },
    ],
  },
  {
    title: 'Quản lý quyền',
    isExpanded: false,
    icon: 'person',
    sideBarItems: [
      {
        id: 'roles',
        title: 'Quản lý quyền',
        url: '/cms/roles',
        isSelected: false,
      },
    ],
  },
  {
    title: 'Quản lý giáo trình',
    isExpanded: false,
    icon: 'book',
    sideBarItems: [
      {
        id: 'products',
        title: 'Quản lý giáo trình',
        url: '/cms/products',
        isSelected: false,
      },
    ],
  },
  {
    title: 'Quản lý loại giáo trình',
    isExpanded: false,
    icon: 'menu_book',
    sideBarItems: [
      {
        id: 'category',
        title: 'Quản lý loại giáo trình',
        url: '/cms/category',
        isSelected: false,
      },
    ],
  },
  {
    title: 'Quản lý đơn hàng',
    isExpanded: false,
    icon: 'receipt_long',
    sideBarItems: [
      {
        id: 'bills',
        title: 'Quản lý đơn hàng',
        url: '/cms/bills',
        isSelected: false,
      },
    ],
  },
  {
    title: 'Quản lý đánh giá',
    isExpanded: false,
    icon: 'preview',
    sideBarItems: [
      {
        id: 'reviews',
        title: 'Quản lý đánh giá',
        url: '/cms/reviews',
        isSelected: false,
      },
    ],
  },
  // {
  //   title: 'Quản lý liên hệ',
  //   isExpanded: false,
  //   icon: 'contact_mail',
  //   sideBarItems: [
  //     {
  //       id: 'contacts',
  //       title: 'Quản lý liên hệ',
  //       url: '/cms/contacts',
  //       isSelected: false,
  //     },
  //   ],
  // },
  // {
  //   title: 'Quản lý banner',
  //   isExpanded: false,
  //   icon: 'slideshow',
  //   sideBarItems: [
  //     {
  //       id: 'banner',
  //       title: 'Quản lý banner',
  //       url: '/cms/banner',
  //       isSelected: false,
  //     },
  //   ],
  // },
];
export const SideBarProductList: SideBarItem[] = [
  {
    id: 'trang-chu',
    title: 'Trang chủ',
    url: '/home-page',
    isSelected: false,
    type: 1,
    icon: 'home',
  },
  {
    id: 'sale-product',
    title: 'Giáo trình giảm giá',
    url: '/category/sale-product',
    isSelected: false,
    type: 1,
    icon: 'sell',
  },
  {
    id: 'new-product',
    title: 'Giáo trình mới',
    url: '/category/new-product',
    isSelected: false,
    type: 1,
    icon: 'verified',
  },
  {
    id: 'old-product',
    title: 'Giáo trình cũ',
    url: '/category/old-product',
    isSelected: false,
    type: 1,
    icon: 'history',
  },
  {
    id: 'best-seller',
    title: 'Giáo trình bán chạy',
    url: '/category/best-seller',
    isSelected: false,
    type: 1,
    icon: 'class',
  },
  {
    id: 'favorite-product',
    title: 'Giáo trình yêu thích',
    url: '/category/favorite-product',
    isSelected: false,
    type: 1,
    icon: 'favorite',
  },
];
