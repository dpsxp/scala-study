var mod = angular.module('app', [])

var createProduct = function() {
  return {
    name: "",
    price: ""
  }
}

mod.controller('Products', ["$scope", "$http", "$httpParamSerializer", function($scope, $http, $httpParamSerializer) {
  $scope.products = [];

  $scope.resetProduct = function() {
    $scope.product = createProduct();
  }

  $scope.resetProduct();

  $http.get('/products').then(function(response) {
    $scope.products = response.data;
  });

  $scope.edit = function(product) {
    $scope.product = product;
  }

  $scope.submit = function() {
    event.preventDefault();
    var product = $scope.product;

    if ("id" in product) {
      $scope.update(product);
    } else {
      $scope.save(product);
    }
  }

  $scope.update = function(product) {
    var req = {
      method: "PUT",
      url: "/products/" + product.id,
      data: $httpParamSerializer(product)
    };

    $http(req)
      .then(function(response) {
        $scope.resetProduct()
      });
  }

  $scope.save = function(product) {
    var req = {
      method: "POST",
      url: "/products",
      data: $httpParamSerializer(product)
    };

    $http(req)
      .then(function(response) {
        $scope.products.push(response.data.product);
      })
      .then(function() {
        $scope.resetProduct()
      });
  };

  $scope.remove = function(id) {
    $http.delete("/products/" + id).then(function(response) {
      $scope.products = $scope.products.filter(function(prod) {
        return prod.id !== id;
      });
    });
  };
}]);

mod.run(["$http", function($http) {
  $http.defaults.headers.post["Content-Type"] = 'application/x-www-form-urlencoded';
  $http.defaults.headers.put["Content-Type"] = 'application/x-www-form-urlencoded';
}])
