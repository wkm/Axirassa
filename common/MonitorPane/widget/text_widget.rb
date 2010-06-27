# Copyright 2010 - Zanoccio LLC. Axirassa Project.
# All Rights Reserved.

require 'MonitorPane/widget/abstract_widget'

class TextWidget < AbstractWidget
  attr_accessor :text

  def initialize(text)
    @text = text
  end

  def width
    @text.length
  end

  def render(y,x)
    Ncurses.stdscr.mvprintw(y,x, @text)
  end
end
