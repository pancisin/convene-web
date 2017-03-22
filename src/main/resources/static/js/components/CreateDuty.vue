<template>
  <div class="row">
    <div class="col-xs-12">
      <div class="card">
        <div class="card-header">Users</div>
        <div class="card-body">
          
        <form class="form form-horizontal">
            <div class="section">
                <div class="section-title">Information</div>
                <div class="section-body">
                <div class="form-group">
                    <label class="col-md-3 control-label">Location</label>
                    <div class="col-md-9">
                      <input type="text" class="form-control" placeholder="">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-md-3">
                    <label class="control-label">Starts</label>
                    <p class="control-label-help">( short detail of products , 150 max words )</p>
                    </div>
                    <div class="col-md-9">
                    <textarea class="form-control"></textarea>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-3 control-label">Price</label>
                    <div class="col-md-9">
                    <div class="input-group">
                        <span class="input-group-addon">$</span>
                        <input type="text" class="form-control" aria-label="Amount (to the nearest dollar)">
                    </div>
                    </div>
                </div>
                </div>
                <div class="form-group">
                  <label class="col-md-3 control-label">Price</label>
                    <div class="col-md-9">
                   <select>
                    <option>meno zamestnanca</option>
                  </select>
                  </div>
                </div>
            </div>
            <div class="section">
                <div class="section-title">Warranty</div>
                <div class="section-body">
                <div class="form-group">
                    <label class="col-md-3 control-label">Type</label>
                    <div class="col-md-9">
                    <div class="radio radio-inline">
                        <input type="radio" name="radio4" id="radio10" value="option10">
                        <label for="radio10">
                            Global
                        </label>
                    </div>
                    <div class="radio radio-inline">
                        <input type="radio" name="radio4" id="radio11" value="option11" checked="">
                        <label for="radio11">
                            Local
                        </label>
                    </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-3 control-label">Cover</label>
                    <div class="col-md-4">
                    <div class="input-group">
                        <select class="select2 select2-hidden-accessible" tabindex="-1" aria-hidden="true">
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                        </select><span class="select2 select2-container select2-container--default" dir="ltr" style="width: 373px;"><span class="selection"><span class="select2-selection select2-selection--single" role="combobox" aria-haspopup="true" aria-expanded="false" tabindex="0" aria-labelledby="select2-qegl-container"><span class="select2-selection__rendered" id="select2-qegl-container" title="1">1</span><span class="select2-selection__arrow" role="presentation"><b role="presentation"></b></span></span></span><span class="dropdown-wrapper" aria-hidden="true"></span></span>
                        <span class="input-group-addon">Years</span>
                    </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="form-footer">
            <div class="form-group">
            <div class="col-md-9 col-md-offset-3">
                <button type="submit" class="btn btn-primary">Save</button>
                <button type="button" class="btn btn-default">Cancel</button>
            </div>
            </div>
        </div>
        </form>

        </div>
      </div>
    </div>
  </div>
  </template>

<script>
 export default {
    name: 'create-duty',
    data: function() {
        return {
        }
    },
 }
</script>