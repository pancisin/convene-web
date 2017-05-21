<template>
  <div class="container">
    <div class="row">
      <div class="col-sm-12 m-b-20">
        <div class="text-center">
          <h3 class="font-600">Pricing</h3>
          <p class="text-muted"> Please select a package that fits to your bussiness or try free version.</p>
        </div>
      </div>
  
      <div class="col-lg-9 center-page">
        <div class="row">
          <article class="pricing-column col-lg-4 col-md-4">
            <div class="inner-box card-box">
              <div class="plan-header text-center">
                <h3 class="plan-title">Starter Pack</h3>
                <h2 class="plan-price">$9</h2>
                <div class="plan-duration">Per Month</div>
              </div>
              <ul class="plan-stats list-unstyled text-center">
                <li>
                  <i class="ti-cloud text-success"></i> 20 events per month
                </li>
                <li>
                  <i class="ti-briefcase text-success"></i> 1 dedicated page
                </li>
                <li>
                  <i class="ti-briefcase text-success"></i> 3 services per page
                </li>
              </ul>
  
              <div class="text-center">
                <a href="#" class="btn btn-danger btn-bordred btn-rounded waves-effect waves-light">Signup Now</a>
              </div>
            </div>
          </article>
  
          <article class="pricing-column col-lg-4 col-md-4">
            <div class="ribbon">
              <span>POPULAR</span>
            </div>
            <div class="inner-box card-box">
              <div class="plan-header text-center">
                <h3 class="plan-title">Professional Pack</h3>
                <h2 class="plan-price">$29</h2>
                <div class="plan-duration">Per Month</div>
              </div>
              <ul class="plan-stats list-unstyled text-center">
                <li>
                  <i class="ti-cloud text-success"></i> Unlimited events
                </li>
                <li>
                  <i class="ti-briefcase text-success"></i> 10 dedicated pages
                </li>
                <li>
                  <i class="ti-wand text-success"></i> 1 conference per year
                </li>
              </ul>
  
              <div class="text-center">
                <a href="#" class="btn btn-danger btn-bordred btn-rounded waves-effect waves-light">Signup Now</a>
              </div>
            </div>
          </article>
  
          <article class="pricing-column col-lg-4 col-md-4">
            <div class="inner-box card-box">
              <div class="plan-header text-center">
                <h3 class="plan-title">Enterprise Pack</h3>
                <h2 class="plan-price">$89</h2>
                <div class="plan-duration">Per Month</div>
              </div>
              <ul class="plan-stats list-unstyled text-center">
                <li>
                  <i class="ti-wand text-success"></i> Unlimited events
                </li>
                <li>
                  <i class="ti-cloud text-success"></i> Unlimited pages
                </li>
                <li>
                  <i class="ti-cloud text-success"></i> Unlimited services
                </li>
                <li>
                  <i class="ti-briefcase text-success"></i> Unlimited conferences
                </li>
                <li>
                  <i class="ti-headphone-alt text-success"></i> 24x7 Support
                </li>
              </ul>
  
              <div class="text-center">
                <button disabled class="btn btn-danger btn-bordred btn-rounded waves-effect waves-light btn-disabled">Signup Now</button>
              </div>
            </div>
          </article>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'pricing'
}
</script>