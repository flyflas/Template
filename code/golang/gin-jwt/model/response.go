package model

import (
	"github.com/gin-gonic/gin"
	"net/http"
)

type Response struct {
	Code int         `json:"code"`
	Msg  string      `json:"msg"`
	Data interface{} `json:"data"`
}

func ReturnResponse(c *gin.Context, code int, msg string, data interface{}) {
	response := &Response{
		Code: code,
		Msg:  msg,
		Data: data,
	}
	c.JSON(http.StatusOK, response)
}

func ReturnResponseByHttpStatus(c *gin.Context, code int, data interface{}) {
	ReturnResponse(c, code, http.StatusText(code), data)
}
