desc 'Continuous Integration task'
task 'ci' do
  sh 'bundle exec buildr clean package'
end
