//
//  disciple.m
//  testApp
//
//  Created by Lucy Hutcheson on 11/12/13.
//  Copyright (c) 2013 Lucy Hutcheson. All rights reserved.
//

#import "disciple.h"

@implementation disciple

@synthesize discipleID, first, last, phone, email, age, discipleDictionary;

static disciple *_instance = nil;

+(void)CreateInstance
{
    if (_instance == nil)
    {
        [[self alloc] init];
    }
}


+(disciple*)GetInstance
{
    return _instance;
}

+(id)alloc
{
    _instance = [super alloc];
    return _instance;
}

-(id)init
{
    if (self = [super init])
    {
        // init code
    }
    return self;
}

-(NSDictionary*)getDictionary
{
    if (discipleDictionary != nil)
    {
        return discipleDictionary;
    }
    return NULL;
}


@end
