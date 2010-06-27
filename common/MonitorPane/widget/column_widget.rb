# Copyright 2010 - Zanoccio LLC. Axirassa Project.
# All Rights Reserved.

require 'MonitorPane/widget/abstract_widget'

# displays widgets in a column
class ColumnWidget < AbstractWidget
  attr_reader :widgets

  def initialize(*widgets)
    @widgets = []
    add_widgets(*widgets)
  end

  def add_widgets(*widgets)
    @widgets = @widgets + widgets
  end

  def render(y,x)
    currenty = y
    @widgets.each do |widget|
      widget.render(currenty,x)
      currenty += widget.height
    end
  end
end
