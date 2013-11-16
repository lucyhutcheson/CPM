//
//  disciple.h
//  testApp
//
//  Created by Lucy Hutcheson on 11/12/13.
//  Copyright (c) 2013 Lucy Hutcheson. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <Parse/Parse.h>

@interface disciple : NSObject
{
    NSInteger discipleID;
    NSString *first;
    NSString *last;
    NSString *phone;
    NSString *email;
    NSNumber *age;
    NSDictionary *discipleDictionary;
}

@property (nonatomic, assign) NSInteger discipleID;
@property (nonatomic, retain) NSString *first;
@property (nonatomic, retain) NSString *last;
@property (nonatomic, retain) NSString *phone;
@property (nonatomic, retain) NSString *email;
@property (nonatomic, retain) NSNumber *age;
@property (nonatomic) NSDictionary *discipleDictionary;

+(void)CreateInstance;

+(disciple*)GetInstance;

-(NSDictionary*)getDictionary;

@end
