package initialize

import (
	"fmt"
	"go.uber.org/zap"
	"go.uber.org/zap/zapcore"
)

const (
	Black Color = iota + 30
	Red
	Green
	Yellow
	Blue
	Magenta
	Cyan
	White
)

const (
	BlackBackgroundColor Color = iota + 40
	RedBackgroundColor
	GreenBackgroundColor
	YellowBackgroundColor
	BlueBackgroundColor
	MagentaBackgroundColor
	CyanBackgroundColor
	WhiteBackgroundColor
)

// Color represents a text color.
type Color uint8

// Add adds the coloring to the given string.
func (c Color) Add(s string) string {
	return fmt.Sprintf("\x1b[%dm\x1b[%dm %s \x1b[0m", uint8(Black), uint8(c), s)
}

func (c Color) SetColorText(s string) string {
	return fmt.Sprintf("\x1b[%dm %s \x1b[0m", uint8(c), s)
}

var (
	_levelToColor = map[zapcore.Level]Color{
		zap.DebugLevel:  MagentaBackgroundColor,
		zap.InfoLevel:   BlueBackgroundColor,
		zap.WarnLevel:   YellowBackgroundColor,
		zap.ErrorLevel:  RedBackgroundColor,
		zap.DPanicLevel: RedBackgroundColor,
		zap.PanicLevel:  RedBackgroundColor,
		zap.FatalLevel:  RedBackgroundColor,
	}
	_unknownLevelColor = RedBackgroundColor

	_levelToLowercaseColorString = make(map[zapcore.Level]string, len(_levelToColor))
	_levelToCapitalColorString   = make(map[zapcore.Level]string, len(_levelToColor))
)

func init() {
	for level, color := range _levelToColor {
		_levelToLowercaseColorString[level] = color.Add(level.String()) + " |"
		_levelToCapitalColorString[level] = color.Add(level.CapitalString()) + " |"
	}
}

func CapitalColorLevelEncoder(l zapcore.Level, enc zapcore.PrimitiveArrayEncoder) {
	s, ok := _levelToCapitalColorString[l]
	if !ok {
		s = _unknownLevelColor.Add(l.CapitalString())
	}
	enc.AppendString(s)
}
