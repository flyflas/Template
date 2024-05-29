package router

import (
	"demo/global"
	"demo/model"
	"golang.org/x/crypto/bcrypt"
	"log"
	"os"
	"time"

	jwt "github.com/appleboy/gin-jwt/v2"
	"github.com/gin-gonic/gin"
)

type login struct {
	Username string `form:"username" json:"username" binding:"required"`
	Password string `form:"password" json:"password" binding:"required"`
}

var (
	identityKey = "email"
	port        string
)

func init() {
	port = os.Getenv("PORT")
	if port == "" {
		port = "8000"
	}
}

//func main() {
//	engine := gin.Default()
//	// the jwt middleware
//	authMiddleware, err := jwt.New(initParams())
//	if err != nil {
//		log.Fatal("JWT Error:" + err.Error())
//	}
//
//	// register middleware
//	engine.Use(handlerMiddleWare(authMiddleware))
//
//	// register route
//	registerRoute(engine, authMiddleware)
//
//	// start http server
//	if err = http.ListenAndServe(":"+port, engine); err != nil {
//		log.Fatal(err)
//	}
//}
//
//func registerRoute(r *gin.Engine, handle *jwt.GinJWTMiddleware) {
//	r.POST("/login", handle.LoginHandler)
//	r.NoRoute(handle.MiddlewareFunc(), handleNoRoute())
//
//	auth := r.Group("/auth", handle.MiddlewareFunc())
//	auth.GET("/refresh_token", handle.RefreshHandler)
//	auth.GET("/hello", helloHandler)
//}
//

func HandlerMiddleWare(authMiddleware *jwt.GinJWTMiddleware) gin.HandlerFunc {
	return func(context *gin.Context) {
		errInit := authMiddleware.MiddlewareInit()
		if errInit != nil {
			global.Logger.Error("authMiddleware.MiddlewareInit() Error:" + errInit.Error())
		}
	}
}

func InitParams() *jwt.GinJWTMiddleware {

	return &jwt.GinJWTMiddleware{
		Realm:       "/auth",
		Key:         []byte(global.ApplicationSetting.JwtSetting.Secret),
		Timeout:     time.Hour * time.Duration(global.ApplicationSetting.JwtSetting.Expire),
		MaxRefresh:  time.Second,
		IdentityKey: identityKey,
		PayloadFunc: payloadFunc(),

		IdentityHandler: identityHandler(),
		// 登录验证
		Authenticator: authenticator(),
		// 权限认证
		Authorizator:  authorizator(),
		Unauthorized:  unauthorized(),
		TokenLookup:   "header: Authorization",
		TokenHeadName: "Bearer",
		TimeFunc:      time.Now,
		LoginResponse: loginResponse,
	}
}

func payloadFunc() func(data interface{}) jwt.MapClaims {
	return func(data interface{}) jwt.MapClaims {
		if v, ok := data.(*model.User); ok {
			return jwt.MapClaims{
				identityKey: v.Email,
				"iss":       global.ApplicationSetting.JwtSetting.Issuer,
			}
		}
		return jwt.MapClaims{}
	}
}

func identityHandler() func(c *gin.Context) interface{} {
	return func(c *gin.Context) interface{} {
		claims := jwt.ExtractClaims(c)
		return &model.User{
			Email: claims[identityKey].(string),
		}
	}
}

func authenticator() func(c *gin.Context) (interface{}, error) {
	return func(c *gin.Context) (interface{}, error) {
		var loginVals login
		if err := c.ShouldBind(&loginVals); err != nil {
			return "", jwt.ErrMissingLoginValues
		}
		email := loginVals.Username
		password := loginVals.Password

		// 数据库查询
		var user *model.User
		result := global.DB.Where("email = ?", email).Take(&user)
		if result.Error != nil {
			return nil, jwt.ErrFailedAuthentication
		}

		err := bcrypt.CompareHashAndPassword([]byte(user.Password), []byte(password))
		if err != nil {
			return nil, jwt.ErrFailedAuthentication
		}

		user.Password = ""

		return user, nil
	}
}

func authorizator() func(data interface{}, c *gin.Context) bool {
	return func(data interface{}, c *gin.Context) bool {
		return true
		//if v, ok := data.(*User); ok && v.UserName == "admin" {
		//	return true
		//}
		//return false
	}
}

func unauthorized() func(c *gin.Context, code int, message string) {
	return func(c *gin.Context, code int, message string) {
		model.ReturnResponseByHttpStatus(c, code, message)
	}
}

func loginResponse(c *gin.Context, code int, message string, time time.Time) {
	res := make(map[string]interface{})
	res["token"] = message
	res["expire"] = time
	model.ReturnResponseByHttpStatus(c, code, res)
}

func handleNoRoute() func(c *gin.Context) {
	return func(c *gin.Context) {
		claims := jwt.ExtractClaims(c)
		log.Printf("NoRoute claims: %#v\n", claims)
		c.JSON(404, gin.H{"code": "PAGE_NOT_FOUND", "message": "Page not found"})
	}
}
