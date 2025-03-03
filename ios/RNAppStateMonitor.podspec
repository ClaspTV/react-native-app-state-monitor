require 'json'

Pod::Spec.new do |s|
  package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

  s.name         = 'RNAppStateMonitor'
  s.version      = package['version']
  s.summary      = package['description']
  s.license      = package['license']
  s.author        = { "Vizbee" => "info@vizbee.tv" }
  s.homepage     = package['repository']['url']
  s.platform     = :ios, '12.0'
  s.source       = { :git => package['repository']['url'], :tag => s.version.to_s }
  s.source_files = 'ios/*.{h,m}'
  s.requires_arc = true
  s.dependency 'React-Core'
end