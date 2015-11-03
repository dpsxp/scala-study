describe('ProductsResource', function() {
  var $httpBackend, id = 30;

  beforeEach(module('app'));

  beforeEach(inject(function($injector) {
    $httpBackend = $injector.get('$httpBackend');
    $httpBackend
      .when('PUT', '/products/' + id)
      .respond({ name: 'fake', price: 30 });
  }));

  describe('#update', function() {
    it('call a update with a PUT method on server', inject(function(ProductsResource) {
      $httpBackend.expectPUT('/products/' + id);
      ProductsResource.update({ id: id });
      $httpBackend.flush();
    }));
  });
});
