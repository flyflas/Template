package initialize

import (
	"demo/global"
	"demo/router"
	"fmt"
	"go.uber.org/zap"
	"go.uber.org/zap/zapcore"
	"gopkg.in/yaml.v3"
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
	"gorm.io/gorm/schema"
	"moul.io/zapgorm2"
	"os"
	"time"
)

func ReadApplicationFile() {
	if global.Logger == nil {
		panic("ReadApplicationFile: Logger 没有初始化，请检查初始化顺序是否正确!!!")
	}

	file, err := os.ReadFile("application.yaml")
	if err != nil {
		global.Logger.Errorf("application.yaml  配置文件读取失败!!! Error: %s ", err)
		panic(nil)
	}

	yamlErr := yaml.Unmarshal(file, &global.ApplicationSetting)
	if yamlErr != nil {
		global.Logger.Errorf("application.yaml 配置文件错误!!! Error: %s", yamlErr)
		panic(nil)
	}
	global.Logger.Info("Application.yaml 读取成功")
}

func InitGorm() {
	logger := zapgorm2.New(global.Logger.Desugar())
	logger.SetAsDefault()
	gormSetting := global.ApplicationSetting.GormSetting
	dsn := fmt.Sprintf("%s:%s@tcp(%s:%s)/%s?charset=utf8mb4&parseTime=True",
		gormSetting.Username,
		gormSetting.Password,
		gormSetting.Host,
		gormSetting.Port,
		gormSetting.Database)

	db, err := gorm.Open(mysql.Open(dsn), &gorm.Config{
		NamingStrategy: schema.NamingStrategy{
			SingularTable: true,
		},
		Logger: logger,
	})
	if err != nil {
		global.Logger.Errorf("数据库连接错误!!! Error: %s", err)
		panic(nil)
	}

	sqlDB, dbError := db.DB()
	if dbError != nil {
		global.Logger.Errorf("数据库连接错误!!! Error: %s", dbError)
		panic(nil)
	}

	// 设置连接池
	sqlDB.SetMaxIdleConns(10)
	sqlDB.SetMaxOpenConns(100)
	sqlDB.SetConnMaxLifetime(time.Hour)

	global.DB = db
	global.Logger.Info("数据库配置成功")
}

func InitGin() {
	r := router.UseRouter()

	err := r.Run(fmt.Sprintf("%s:%s", global.ApplicationSetting.GinSetting.Host, global.ApplicationSetting.GinSetting.Port))
	if err != nil {
		global.Logger.Errorf("Gin运行失败!!! Error: %s", err)
	}
}

func InitLogger() {
	zapLoggerEncoderConfig := zapcore.EncoderConfig{
		TimeKey:          "time",
		LevelKey:         "level",
		NameKey:          "logger",
		CallerKey:        "caller",
		MessageKey:       "message",
		StacktraceKey:    "stacktrace",
		EncodeCaller:     customCallerEncoder,
		EncodeTime:       customTimeEncoder,
		EncodeLevel:      CapitalColorLevelEncoder,
		EncodeDuration:   zapcore.SecondsDurationEncoder,
		LineEnding:       "\n",
		ConsoleSeparator: " ",
	}

	global.Logger = zap.New(zapcore.NewCore(zapcore.NewConsoleEncoder(zapLoggerEncoderConfig), zapcore.AddSync(os.Stdout), zap.DebugLevel), zap.AddCaller()).Sugar()
}

func customTimeEncoder(time time.Time, encoder zapcore.PrimitiveArrayEncoder) {
	encoder.AppendString(Color.SetColorText(Green, time.Format("2006-01-02 15:04:05.000")) + " |")
}

func customCallerEncoder(caller zapcore.EntryCaller, encoder zapcore.PrimitiveArrayEncoder) {
	encoder.AppendString("[" + caller.TrimmedPath() + "] |")
}
