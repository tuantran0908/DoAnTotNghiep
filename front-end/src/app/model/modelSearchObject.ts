export class ProductGroupSearchRequest {
  code: string | undefined;
  name: string | undefined;
  nameEn: string | undefined;
  status: string | undefined;
  productType: string | undefined;
};


export class ProductSearchRequest {
  code: string | undefined;
  name: string | undefined;
  status: string | undefined;
  productType: string | undefined;
  productGroup: string | undefined;
};


export class PackageSearchRequest {
  code: string | undefined;
  name: string | undefined;
  ispublic: string | undefined;
  startDate: Date | undefined;
  startDateFrom: Date | undefined;
  startDateTo: Date | undefined;
  endDate: Date | undefined;
  endDateFrom: Date | undefined;
  endDateTo: Date | undefined;
};
