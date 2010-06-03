#!/usr/bin/env ruby
#
# Monitors the message passing system, providing a realtime feed of the 
# AX system status
#
#

require 'rubygems'
require 'ncurses'
require 'term/ansicolor'

class String
	include Term::ANSIColor
end

module AX
	module Sentinel
		def time_string
			return Time.new.strftime('%Y-%b-%d %a %I:%M:%S%p')
		end
	
	  $colwidth = 70
	  
	  begin
			Ncurses.initscr()
			Ncurses.raw()
			Ncurses.noecho()
			Ncurses.stdscr.keypad(true)
			
		#	while(true) do
				Ncurses.printw(" #{'ax|sentinel'.bold}    every .2sec  [#{Time.new.strftime('%Y-%b-%d %a %I:%M:%S%p')}] ")
				Ncurses.nl
				Ncurses.printw(" pinger".ljust($colwidth).yellow.bold.on_blue + '|'.bold + " pinger_reply".ljust($colwidth).yellow.bold.on_blue)
				Ncurses.nl
		
				30.times do
					puts ' '*$colwidth + '|'.bold
					Ncurses.nl
				end
			
				sleep(2)
			#end
		ensure
			Ncurses.echo
			Ncurses.nocbreak
			Ncurses.nl
			Ncurses.endwin
		end
	end
end
