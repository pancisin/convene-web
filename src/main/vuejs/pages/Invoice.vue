<template>
  <div class="container">
    <div class="panel panel-default">
      <div class="panel-body">
        <div class="clearfix">
          <product-logo large inline />
          <div class="pull-right">
            <h4>Invoice #
              <br>
              <strong v-text="invoice.id"></strong>
            </h4>
          </div>
        </div>
        <hr>
        <div class="row">
          <div class="col-md-12">
  
            <div class="pull-left m-t-30">
              <address>
                <strong>Twitter, Inc.</strong>
                <br> 795 Folsom Ave, Suite 600
                <br> San Francisco, CA 94107
                <br>
                <abbr title="Phone">P:</abbr> (123) 456-7890
              </address>
            </div>
            <div class="pull-right m-t-30">
              <p>
                <strong>Order Date: </strong> {{ invoice.acquired | luxon('ff') }}</p>
              <p class="m-t-10">
                <strong>Order Status: </strong>
                {{ $t(invoice.state.code) }}
              </p>
            </div>
          </div>
        </div>
        <div class="m-h-50"></div>
        <div class="row">
          <div class="col-md-12">
            <div class="table-responsive">
              <table class="table m-t-30">
                <thead>
                  <tr>
                    <th>#</th>
                    <th>Product</th>
                    <th>Billing period</th>
                    <th>Unit Cost</th>
                    <th>Total</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>1</td>
                    <td>convene Premium license</td>
                    <td>per month</td>
                    <td>{{ invoice.subscription.price }}
                      <i class="fa fa-euro"></i>
                    </td>
                    <td>{{ invoice.subscription.price }}
                      <i class="fa fa-euro"></i>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
        <div class="row">
          <div class="col-md-6 col-sm-6 col-xs-6">
            <div class="clearfix m-t-40">
              <h5 class="small text-inverse">PAYMENT TERMS AND POLICIES</h5>
  
              <small>
                All accounts are to be paid within 7 days from receipt of invoice. To be paid by cheque or credit card or direct payment online. If account is not paid within 7 days the credits details supplied as confirmation of work undertaken will be charged the agreed quoted fee noted above.
              </small>
            </div>
          </div>
          <div class="col-md-3 col-sm-6 col-md-offset-3 col-xs-6">
            <p class="text-right">
              <b>Sub-total:</b> {{ invoice.subscription.price }}</p>
            <p class="text-right">Discout: 0.0%</p>
            <p class="text-right">VAT: 19.0%</p>
            <hr>
            <h3 class="text-right">
  
              <i class="fa fa-euro"></i>
              {{ (invoice.subscription.price + (invoice.subscription.price / tax)).toLocaleString() }}
            </h3>
          </div>
        </div>
        <hr>
        <div class="hidden-print">
          <div class="pull-right">
            <a href="javascript:window.print()" class="btn btn-inverse waves-effect waves-light">
              <i class="fa fa-print"></i>
            </a>
            <a href="#" class="btn btn-primary waves-effect waves-light">Submit</a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ProductLogo } from 'elements';
import LicenseApi from 'api/license.api';

export default {
  name: 'invoice',
  data () {
    return {
      invoice: {},
      tax: 19.0
    };
  },
  created () {
    this.getInvoice();
  },
  components: {
    ProductLogo
  },
  methods: {
    getInvoice () {
      var invoice_id = this.$route.params.invoice_id;

      LicenseApi.getLicense(invoice_id, license => {
        this.invoice = license;
      });
    }
  }
};
</script>