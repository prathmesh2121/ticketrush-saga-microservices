--!nocheck


  redis.call('SET', KEYS[1], ARGV[1], 'EX',
    ARGV[2])
  return 1
