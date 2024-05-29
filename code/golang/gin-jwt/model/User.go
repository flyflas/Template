package model

import "gorm.io/plugin/soft_delete"

type User struct {
	UserId    string `gorm:"primaryKey"`
	Username  string
	Password  string
	Age       int
	Gender    string
	Email     string
	Phone     string
	Balance   float64
	IsDeleted soft_delete.DeletedAt `gorm:"softDelete:flag"`
}
