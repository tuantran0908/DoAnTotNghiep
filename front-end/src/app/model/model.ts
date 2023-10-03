export interface Breadcrumb {
  label: string;
  url: string;
}
export class BaseResponse<T> {
  [x: string]: any;
  errorCode?: string;
  errorMessage?: string;
  data?: T;
}

export class BaseResponseList<T> {
  [x: string]: any;
  errorCode?: string;
  errorMessage?: string;
  data?: T[];
}

export class BasePagingResponse<T> {
  errorCode?: string;
  errorMessage?: string;
  data?: PageResponse<T>;
}
export class PageResponse<T> {
  first: boolean = false;
  last: boolean = false;
  totalPages?: number;
  totalElements?: number;
  size?: number;
  number?: number;
  numberOfElements?: number;
  content?: T[];
}

export class Auditable {
  createdBy: User | undefined;
  createdAt: Date | undefined;
  updatedBy: User | undefined;
  updatedAt: Date | undefined;
}

export class User extends Auditable {
  id?: number;
  birthday: string | undefined;
  gender: string | undefined;
  phoneNumber: string | undefined;
  email: string | undefined;
  fullname: string | undefined;
  password: string | undefined;
  status: string | undefined;
  username: string | undefined;
  refreshtokens: Refreshtoken[] | undefined;
  rolesGroup: RolesGroup | undefined;
  avatar: any;
}
export class Refreshtoken extends Auditable {
  id?: number;
  expiryDate: Date | undefined;
  token: string | undefined;
  user: User | undefined;
}
export class RolesGroup extends Auditable {
  id?: number;
  description: string | undefined;
  name: string | undefined;
  status: string | undefined;
  users: User[] | undefined;
  rolesGroup_Roles: RolesGroupRoles[] | undefined;
}
export class RolesGroupRoles extends Auditable {
  id?: number;
  rolesGroup: RolesGroup | undefined;
  roles: Roles | undefined;
}
export class Roles extends Auditable {
  id?: number;
  description: string | undefined;
  name: string | undefined;
  status: string | undefined;
  value: boolean = false;
  rolesGroup_Roles: RolesGroupRoles[] | undefined;
}

export class Review extends Auditable {
  id?: number;
  star: number | undefined;
  message: string | undefined;
  user: User | undefined;
  giaoTrinh: GiaoTrinh | undefined;
}
export class GiaoTrinh extends Auditable {
  id?: number;
  name: string | undefined;
  author: string | undefined;
  description: string | undefined;
  height: number | undefined;
  width: number | undefined;
  length: number | undefined;
  weight: number | undefined;
  price: number | undefined;
  avgStar: number | undefined;
  quantityRemaining: number | undefined;
  quantitySell: number | undefined;
  image: any;
  publicDate: Date | undefined;
  status: string | undefined;
  isNew: string | undefined;
  isFavorite: boolean | undefined;
  sales: number | undefined;
  billDetails: BillDetail[] | undefined;
  cartDetail: CartDetail[] | undefined;
  review: Review[] | undefined;
  category: Category | undefined;
}

export class Category extends Auditable {
  id?: number;
  description: string | undefined;
  name: string | undefined;
  url: string | undefined;
  status: string | undefined;
  giaoTrinh: GiaoTrinh[] | undefined;
}

export class CartDetail extends Auditable {
  id?: number;
  quantity: number | undefined;
  giaoTrinh: GiaoTrinh | undefined;
  user: User | undefined;
}

export class BillDetail extends Auditable {
  id?: number;
  quantity: number | undefined;
  giaoTrinh: GiaoTrinh | undefined;
  bill: Bill | undefined;
}

export class Bill extends Auditable {
  id?: number;
  address: string | undefined;
  name: string | undefined;
  payment: number | string | undefined;
  phoneNumber: string | undefined;
  status: string | undefined;
  total: number | undefined;
  user: User | undefined;
  billDetails: BillDetail[] | undefined;
}

export class ProductsGroup {
  title: string | undefined;
  url: string | undefined;
  products: GiaoTrinh[] | undefined;
}
export class UserSearchRequest {
  id?: number;
  email: string | undefined;
  name: string | undefined;
  status: string | undefined;
  rolesGroupIds: string | undefined;
  createdAtFrom: Date | undefined;
  createdAtTo: Date | undefined;
}

export class ReviewSearchRequest {
  id?: number;
  star: number | undefined;
  message: string | undefined;
  userId: string | undefined;
  giaoTrinhId: string | undefined;
}

export class RolesGroupSearchRequest {
  id?: number;
  description: string | undefined;
  name: string | undefined;
  status: string | undefined;
  rolesIds: string | undefined;
}

export class GiaoTrinhSearchRequest {
  id?: number;
  author: string | undefined;
  name: string | undefined;
  description: string | undefined;
  status: string | undefined;
  publicDateFrom: Date | undefined;
  publicDateTo: Date | undefined;
  categoryIds: string | undefined;
  saleProduct: boolean = false;
  isNew: string | undefined;
  bestSeller: boolean = false;
  lastestProduct: boolean = false;
  favoriteProduct: boolean = false;
  star: number | undefined;
  priceTo: number | undefined;
  priceFrom: number | undefined;
}

export class CartDetailSearchRequest {
  id?: number;
  userId: number | undefined;
}

export class BillSearchRequest {
  id?: number;
  userId?: number;
  name: string | undefined;
  payment: number | undefined;
  phoneNumber: string | undefined;
  status: string | undefined;
}

export class ReportSearchRequest {
  dateFrom: Date | undefined;
  dateTo: Date | undefined;
}

export class PagesRequest {
  curentPage = 0;
  size?: number;
  sort: string | undefined;
}

export class LoginRequest {
  username: string | undefined;
  password: string | undefined;
  captcha: string | undefined;
}

export class LoginResponse {
  errorCode: string | undefined;
  errorMessage: string | undefined;
  token: string | undefined;
  type: string | undefined;
  refreshToken: string | undefined;
  id: number | undefined;
  username: string | undefined;
  fullname: string | undefined;
  email: string | undefined;
  roles: string[] | undefined;
}

export interface NgParam {
  key: string;
  value: string;
}
export class Banner extends Auditable {
  id?: number;
  status: string | undefined;

  name: string | undefined;
  type: string | undefined;
  url: string | undefined;
}
