package main

import (
	"demo/initialize"
)

func main() {
	initialize.InitLogger()
	initialize.ReadApplicationFile()
	initialize.InitGorm()
	initialize.InitGin()
}
