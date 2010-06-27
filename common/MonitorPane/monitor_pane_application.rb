#!/usr/bin/env ruby

require 'rubygems'
require 'ncurses'
require 'singleton'

require 'MonitorPane/widget/widget_container'
require 'MonitorPane/widget/row_widget'
require 'MonitorPane/widget/column_widget'
require 'MonitorPane/widget/label_widget'
require 'MonitorPane/widget/dynamic_label_widget'
require 'MonitorPane/widget/grid_widget'

class MonitorPaneApplication
  include Singleton

  def initialization
    grid = GridWidget.new(
      [
        [LabelWidget.new('value'), TextWidget.new('12')],
        [LabelWidget.new('summation'), TextWidget.new('13123123')]
      ]
    )
    grid.alignment = [:left, :right]

    @container = WidgetContainer.new(
      0, 0,
      grid
    )
  end

  def live_loop
    @container.render
  end

  def run
    Ncurses.initscr
    Ncurses.cbreak

    Ncurses.noecho
    Ncurses.nonl

    Ncurses.start_color

    Ncurses.stdscr.intrflush(false)
    Ncurses.stdscr.keypad(true)
    Ncurses.halfdelay(2)

    set_colors

    initialization

    begin
      live_loop
    end while((@key = Ncurses.stdscr.getch) != ?q)

  ensure
    Ncurses.echo
    Ncurses.nocbreak
    Ncurses.nl
    Ncurses.endwin
  end


  def set_colors
    Ncurses.init_pair(1, Ncurses::COLOR_YELLOW, Ncurses::COLOR_BLUE)
    Ncurses.init_pair(2, Ncurses::COLOR_WHITE, Ncurses::COLOR_BLACK)
    Ncurses.init_pair(3, Ncurses::COLOR_WHITE, Ncurses::COLOR_BLACK)
  end
end

$app = MonitorPaneApplication.instance
$app.run