import { SubscriptionResponse } from "./subscription-response.model";

export interface CustomerResponse {
  id: number;
  fullName: string;
  phone: string;
  registrationDate: string;
  activeSubscription: SubscriptionResponse | null;
  subscriptionCount: number;
}
