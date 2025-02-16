import { CustomerResponse } from "./customer-response.model";
import { PackResponse } from "./pack-response.model";

export interface SubscriptionResponse {
  id: number;
  startDate: string;
  active: boolean;
  customer: CustomerResponse;
  pack: PackResponse;
}
