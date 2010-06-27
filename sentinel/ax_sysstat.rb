#!/usr/bin/env ruby
# Copyright 2010 - Zanoccio LLC. Axirassa Project.
# All Rights Reserved.

$:.unshift File.join(File.dirname(__FILE__), '..', 'common')

require 'MonitorPane/monitor_pane_application'

module Axir
  module Sentinel
    class SystemStatusMonitor < MonitorPaneApplication
      def initialization
        
        @container = WidgetContainer.new(0, 0, grid)
      end

      def live_loop
        @container.render
      end
    end
  end
end
