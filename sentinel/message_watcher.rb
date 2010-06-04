
require 'rubygems'
require 'beanstalk-client'

module AX
	module Messaging
	
		class MessageWatcher
			attr_reader :beanstalk
		
			def initialize(connection, pipe)
				@beanstalk = Beanstalk::Pool.new([connection])
			end
			
			def status
				[@beanstalk.reserve]
			end
		end
		
	end
end
