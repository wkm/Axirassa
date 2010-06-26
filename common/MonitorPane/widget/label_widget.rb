#
# An abstraction of a widget containing stylized text
#
# June 2010
# Copyright Zanoccio LLC.  All rights reserved.
#

class LabelWidget < AbstractWidget
  attr_accessor :style
  attr_accessor :text
  attr_accessor :padding

  def initialize(text, style=nil)
    @text = text
    @style = style

    self.padding = 1
  end

  def padding=(sz)
    @padding = sz
    @paddingtxt = ' '*sz
  end

  def width
    @text.length + (2 * @padding)
  end

  def render(y,x)
    Ncurses.stdscr.attron(Ncurses::A_BOLD | Ncurses::COLOR_PAIR(1))
    Ncurses.stdscr.mvprintw(y, x, @paddingtxt + @text + @paddingtxt)
    Ncurses.stdscr.attroff(Ncurses::A_BOLD | Ncurses::COLOR_PAIR(1))
  end
end
