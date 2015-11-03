describe('toMoney filter', function() {
  beforeEach(module("app"));

  var $filter;

  beforeEach(inject(function(_$filter_){
    $filter = _$filter_;
  }));

  it('returns a BRL notation of a number', function() {
    var toReal = $filter('toMoney');
    expect(toReal(30)).toEqual("R$ 30,00");
  });
});
