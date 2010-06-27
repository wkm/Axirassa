# Copyright 2010 - Zanoccio LLC. Axirassa Project.
# All Rights Reserved.

require 'MonitorPane/widget/abstract_widget'

# draws a group of widgets in a grid
class GridWidget < AbstractWidget

  attr_reader :grid
  attr_reader :rowcount
  attr_reader :colcount
  attr_reader :alignment

  
  def initialize(grid, *opts)
    self.grid = grid
    self.alignment = :left
  end


  def grid=(grid)
    @rowcount = grid.length
    @colcount = 0
    grid.each do |row|
      if row.length > @colcount
        @colcount = row.length
      end
    end

    @grid = grid
  end


  def alignment=(setting)
    if setting.kind_of? Symbol
      @alignment = Array.new(@colcount, setting)
    else
      @alignment = setting
    end
  end
  

  def render(y,x)
    # compute column widths and row heights
    colwidths = Array.new(@colcount, 0)
    rowheights = Array.new(@rowcount, 0)

    for colindex in 0...@colcount do
      for rowindex in 0...@rowcount do
        cellwidth = @grid[rowindex][colindex].width
        cellheight = @grid[rowindex][colindex].height

        if colwidths[colindex] < cellwidth
          colwidths[colindex] = cellwidth
        end

        if rowheights[rowindex] < cellheight
          rowheights[colindex] = cellheight
        end
      end
    end


    # render the grid
    cury = y
    for rowindex in 0..(@rowcount-1) do
      curx = x

      for colindex in 0..(@colcount-1) do
        widget = @grid[rowindex][colindex]

        cellalignment = @alignment[colindex]
        ypos = cury

        case cellalignment
        when :left
          xpos = curx

        when :right
          xpos = curx + colwidths[colindex] - widget.width

        when :center
          xpos = curx + (colwidths[colindex] - widget.width) / 2
          xpos = xpos.floor

        else
          throw Exception.new("Unknown alignment: #{cellalignment.inspect}")
        end

        widget.render(ypos, xpos)
        
        curx += colwidths[colindex]
      end

      cury += rowheights[rowindex]
    end
  end
end
