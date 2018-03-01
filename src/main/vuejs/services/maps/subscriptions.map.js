const SubscriptionType = {
  STARTER: 'STARTER',
  PROFESSIONAL: 'PROFESSIONAL',
  ENTERPRISE: 'ENTERPRISE'
};

export default [
  {
    name: 'Starter pack',
    type: SubscriptionType.STARTER,
    features: [
      '5 events / month',
      '1 page'
    ],
    price: 9
  },
  {
    name: 'Premium pack',
    type: SubscriptionType.PROFESSIONAL,
    features: [
      'Unlimited events',
      '5 pages',
      '5 team members',
      'Limited customers chat',
      'Free support'
    ],
    price: 29
  },
  {
    name: 'Enterprise pack',
    type: SubscriptionType.ENTERPRISE,
    features: [
      'Unlimited pages & events',
      'Unlimited conferences',
      'Unlimited team members',
      'Customers chat',
      'Team chat / task board',
      'Free Support'
    ],
    price: 119
  }
];
