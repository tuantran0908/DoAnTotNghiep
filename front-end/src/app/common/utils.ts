import { GenderEnum, IsNewEnum, PublicEnum, StatusEnum } from './constants';

export const statusList: any = [
  { label: 'Hiệu lực', value: StatusEnum.Y },
  { label: 'Không hiệu lực', value: StatusEnum.N },
];

export const isNewList: any = [
  { label: 'Mới', value: IsNewEnum.Y },
  { label: 'Cũ', value: IsNewEnum.N },
];
export const paymentList: any = [
  { label: 'Thanh toán khi nhận hàng', value: StatusEnum.Y },
  { label: 'Online', value: StatusEnum.N },
];
export const genderList: any = [
  { label: 'Male', value: GenderEnum.MALE },
  { label: 'Female', value: GenderEnum.FEMALE },
];
