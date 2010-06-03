#!ruby

$:.unshift File.join(File.dirname(__FILE__), '..', '..')

require 'benchmark'
require 'rubygems'
require 'active_record'
require 'yaml'
require 'mysql'

dbconfig = YAML::load(File.open('../../common/database.yml'))

ActiveRecord::Base.establish_connection(dbconfig['development'])

# create a pinger table
class CreatePinger < ActiveRecord::Migration
  def self.up
    create_table :pings do |t|
      t.column :id, :bigint, :null => false
      t.column :site, :bigint, :null => false
      t.column :account, :bigint, :null => false
      t.column :date, :datetime
      t.column :hashcode, :string, :null => false
      t.column :responsetime, :double, :null => false
      t.column :responsecode, :int, :null => false #404, 403, etc.
    end
  end

	def self.down
		drop_table :pings
	end
end

CreatePinger.migrate :down
CreatePinger.migrate :up


class Ping < ActiveRecord::Base
end

n = 1000
Benchmark.bm(9) do |x|
	x.report do
		n.times do
			ping = Ping.new
			ping.site = rand(100_000_000_000)
			ping.account = rand(100_000_000_000)
			ping.hashcode = rand(2**128).to_s(36)
			ping.responsetime = rand(10)/10
			ping.responsecode = rand(400)
			ping.save
		end
	end
end
