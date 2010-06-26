# To change this template, choose Tools | Templates
# and open the template in the editor.

require 'widget/abstract_widget'

class RowWidget < AbstractWidget
  attr_reader :widgets
  attr_accessor :riffle

  def initialize(*widgets)
    @riffle = ' '
    @widgets = []
    add_widgets(*widgets)
  end

  def add_widgets(*widgets)
    puts 'loading widgets'
    @widgets = @widgets + widgets
  end

  def add_widget(widget)
    @widgets << widget
  end

  def width
    computed_width = 0
    @widgets.each do |widget|
      computed_width += widget.width
      computed_width += @riffle.length
    end

    return computed_width
  end

  def render(y,x)
    @curx = x
    @widgets.each do |widget|
      widget.render(y,@curx)
      @curx += widget.width
      render_riffle(y, @curx)
    end
  end

  def render_riffle(y,x)
    Ncurses.stdscr.mvprintw(y, x, @riffle)
    @curx += @riffle.length
  end
end
