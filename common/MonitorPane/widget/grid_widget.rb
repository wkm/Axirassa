# Copyright 2010 - Zanoccio LLC. Axirassa Project.
# All Rights Reserved.

require 'widget/abstract_widget'
require 'pp'

# draws a group of widgets in a grid
class GridWidget < AbstractWidget
  attr_reader :grid
  attr_reader :rowcount
  attr_reader :colcount

  def initialize(grid, *opts)
    self.grid = grid
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

  def render(y,x)
    # compute column widths and row heights
    colwidths = Array.new(@colcount, 0)
    rowheights = Array.new(@rowcount, 0)

    for colindex in 0..(@colcount-1) do
      for rowindex in 0..(@rowcount-1) do
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
        @grid[rowindex][colindex].render(cury, curx)
        curx += colwidths[colindex]
      end

      cury += rowheights[rowindex]
    end
  end
end
