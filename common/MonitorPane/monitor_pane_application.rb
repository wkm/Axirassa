# To change this template, choose Tools | Templates
# and open the template in the editor.

require 'ncurses'
require 'singleton'

class MonitorPaneApplication
  include Singleton

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


    begin
      Ncurses.stdscr.attron(Ncurses::A_BOLD)
      Ncurses.stdscr.mvprintw(0, 1, 'ax_god')

      Ncurses.stdscr.attroff(Ncurses::A_BOLD)
      Ncurses.stdscr.mvprintw(0, 9, Time.new.strftime('(%Y-%b-%d %a %I:%M:%S%p)'))
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

if $0 == __FILE__
  $app = MonitorPaneApplication.instance
  $app.run
end