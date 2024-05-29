package global

import (
	"demo/model"
	"go.uber.org/zap"
	"gorm.io/gorm"
)

var (
	ApplicationSetting *model.ApplicationSetting
	DB                 *gorm.DB
	Logger             *zap.SugaredLogger
)
