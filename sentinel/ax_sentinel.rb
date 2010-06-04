#!/usr/bin/env ruby
#
# Monitors the message passing system, providing a realtime feed of the 
# AX system status
#
#

require 'rubygems'
require 'ncurses'
require 'term/ansicolor'

require 'message.rb'
require 'message_watcher.rb'

module AX
	module Sentinel
		LA_1 = 'losangeles1'
		HTTP = 'HTTP'
		STORE = ' STORE '
		
		$width = 80
	  
	  $wHeader
	  $wPinger
	  
	  $watcher = Messaging::MessageWatcher.new('127.0.0.1:11130', 'pinger')
	  
	  
	  $messages = [
	  	Pinger::Message.new(1230, LA_1, HTTP, STORE, 'zanoccio.com'),
	  	Pinger::Message.new(1231, LA_1, HTTP, STORE, 'google.com'),
	  	Pinger::Message.new(1232, LA_1, HTTP, STORE, 'wolfram.com')
	  ]
	  
	  begin
			Ncurses.initscr
			Ncurses.cbreak

			Ncurses.noecho
			Ncurses.nonl
			
			Ncurses.start_color
#			Ncurses.raw

			Ncurses.stdscr.intrflush(false)
			Ncurses.stdscr.keypad(true)
			Ncurses.halfdelay(2)
			
			# init colors
			Ncurses.init_pair(1, Ncurses::COLOR_YELLOW, Ncurses::COLOR_BLUE)
			Ncurses.init_pair(2, Ncurses::COLOR_WHITE, Ncurses::COLOR_BLACK)
			
			Ncurses.init_color(Ncurses::COLOR_CYAN, 200, 200, 200)
			Ncurses.init_pair(3, Ncurses::COLOR_WHITE, Ncurses::COLOR_BLACK)
			
			$wHeader = Ncurses::WINDOW.new(1, $width, 0, 0)
			$wPinger = Ncurses::WINDOW.new(0, $width, 1, 0)

	 		$wHeader.attron(Ncurses::A_BOLD)
  		$wHeader.mvprintw(0, 1, 'ax|sentinel')
  		$wHeader.attroff(Ncurses::A_BOLD)
			$wHeader.noutrefresh

			$wPinger.attron(Ncurses::A_BOLD | Ncurses::COLOR_PAIR(1))	
			$wPinger.mvprintw(0, 0, ' Q[pinger] '.ljust(Ncurses.COLS() / 2))
			$wPinger.attroff(Ncurses::A_BOLD)
			Ncurses.doupdate
			
			begin	
				$wHeader.mvprintw(0, 15, Time.new.strftime('%Y-%b-%d %a %I:%M:%S%p'))
				
				
				
				index = 0				
				$messages.each do |msg|
					if(index.odd?)
						$wPinger.attron(Ncurses::COLOR_PAIR(2))
					else
						$wPinger.attron(Ncurses::COLOR_PAIR(3))
					end
				
					$wPinger.mvprintw(1 + index*2, 0, msg.format)
					index += 1
				end
				$wPinger.noutrefresh
			end while((key = $wHeader.getch) != ?q)
		ensure
			Ncurses.echo
			Ncurses.nocbreak
			Ncurses.nl
			Ncurses.endwin
		end
	end
end
