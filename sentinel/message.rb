module AX
	module Pinger
		class Message
			attr_accessor :id
			attr_accessor :server
			attr_accessor :type
			attr_accessor :status
			attr_accessor :url
		
			def initialize(id, server, type, status, url)
				@id = id
				@server = server
				@type = type
				@status = status
				@url = url
			end
		
			def format
				"#{'%08d' % @id}  #{@server}  #{@type}".ljust(71) + "[#{@status}]" + \
				@url
			end
		end
	end
end
