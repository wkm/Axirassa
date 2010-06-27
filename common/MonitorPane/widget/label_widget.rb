#
# An abstraction of a widget containing stylized text
#
# June 2010
# Copyright Zanoccio LLC.  All rights reserved.
#

require 'MonitorPane/widget/text_widget'

class LabelWidget < TextWidget
  attr_accessor :style
  attr_accessor :padding

  def initialize(text, style=nil)
    @originaltext = text
    @style = style

    self.padding = 1
  end

  def padding=(sz)
    @padding = sz
    @paddingtxt = ' '*sz
    self.text = @paddingtxt + @originaltext + @paddingtxt
  end

  def render(y,x)
    Ncurses.stdscr.attron(Ncurses::A_BOLD | Ncurses::COLOR_PAIR(1))
    super(y, x)
    Ncurses.stdscr.attroff(Ncurses::A_BOLD | Ncurses::COLOR_PAIR(1))
  end
end
