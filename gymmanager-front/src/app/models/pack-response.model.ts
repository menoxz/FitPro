export interface PackResponse {
  id: number;
  name: string;
  durationInMonths: number;
  monthlyPrice: number;
}

export interface PackResponseWithDetails extends PackResponse {
  // Ajoutez des détails supplémentaires si nécessaire
}
