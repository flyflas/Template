package model

type ApplicationSetting struct {
	GinSetting  *GinSetting  `yaml:"server"`
	GormSetting *GormSetting `yaml:"gorm"`
	JwtSetting  *JwtSetting  `yaml:"jwt"`
}

type GormSetting struct {
	Host     string `yaml:"host"`
	Port     string `yaml:"port"`
	Username string `yaml:"username"`
	Password string `yaml:"password"`
	Database string `yaml:"database"`
}

type GinSetting struct {
	Host string `yaml:"host"`
	Port string `yaml:"port"`
}

type JwtSetting struct {
	Expire int    `yaml:"expire"`
	Secret string `yaml:"secret"`
	Issuer string `yaml:"issuer"`
}
