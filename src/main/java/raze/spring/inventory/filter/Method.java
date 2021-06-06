package raze.spring.inventory.filter;

public enum Method {
      POST("POST"),
      PUT("PUT"),
      DELETE("DELETE"),
      GET("GET"),
      OPTIONS("OPTIONS"),
      HEAD("HEAD"),
      PATCH("PATCH");

      private final String type;

      Method(String type) {
        this.type = type;
      }
}
