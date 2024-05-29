package router

import (
	"demo/model"
	"demo/router/route"
	jwt "github.com/appleboy/gin-jwt/v2"
	"github.com/gin-gonic/gin"
	"net/http"
)

func UseRouter() *gin.Engine {
	router := gin.Default()

	authMiddleware, jwtErr := jwt.New(InitParams())
	if jwtErr != nil {
		panic("GIN: Jwt验证器 初始化失败!!!")
	}

	router.Use(HandlerMiddleWare(authMiddleware))

	// 设置路由
	route.UseLogin(router, authMiddleware)

	router.GET("/hello", func(context *gin.Context) {
		model.ReturnResponseByHttpStatus(context, http.StatusOK, "HelloWorld")
	})

	router.GET("/test", authMiddleware.MiddlewareFunc(), func(context *gin.Context) {
		model.ReturnResponseByHttpStatus(context, http.StatusOK, "test")
	})

	return router
}
