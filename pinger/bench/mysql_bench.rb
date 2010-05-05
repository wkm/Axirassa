#!ruby

$:.unshift File.join(File.dirname(__FILE__), '..', '..')

require 'rubygems'
require 'active_record'
require 'yaml'
require 'mysql'

dbconfig = YAML::load(File.open('../../common/database.yml'))

ActiveRecord::Base.establish_connection(dbconfig['development'])

class CreatePinger < ActiveRecord::Migration
  def self.up
    create_table :pings do |t|
      t.column :id, :bigint, :null => false
      t.column :site, :bigint, :null => false
      t.column :account, :bigint, :null => false
      t.column :date, :timestamp, :null => false
      t.column :hash, :string, :null => false
      t.column :responsetime, :double, :null => false
      t.column :responsecode, :int, :null => false #404, 403, etc.
    end
  end
end

CreatePinger.migrate :up