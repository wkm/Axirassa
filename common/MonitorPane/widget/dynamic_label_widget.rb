# Copyright 2010 - Zanoccio LLC. Axirassa Project.
# All Rights Reserved.

require 'MonitorPane/widget/label_widget'

module MonitorPane
  module Widget

    class DynamicLabelWidget < LabelWidget
      def initialize(&textfn)
        super('');
        @textfn = textfn
      end

      def render(y,x)
        @text = @textfn.call
        super(y,x)
      end
    end

  end
end
