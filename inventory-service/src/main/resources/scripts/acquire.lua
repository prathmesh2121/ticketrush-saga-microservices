--!nocheck
local v = redis.call('GET', KEYS[1])
if v == ARGV[1] then return 1 end
if not v then
  redis.call('SET', KEYS[1], ARGV[1], 'EX',
    ARGV[2])
  return 1
end
return 0