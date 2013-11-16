//
//  DBManager.h
//  testApp
//
//  Created by Lucy Hutcheson on 11/12/13.
//  Copyright (c) 2013 Lucy Hutcheson. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <sqlite3.h>

@interface DBManager : NSObject
{
    NSString *databasePath;
    NSString *querySQL;
}

+ (DBManager*)getSharedInstance;
- (BOOL)createDB;
- (BOOL)deleteData:(NSString*)first last:(NSString*)last email:(NSString*)email;
- (BOOL)saveData:(NSString*)first last:(NSString*)last email:(NSString*)email phone:(NSString*)phone age:(NSNumber*)age;

- (NSMutableArray*) findByColumn:(NSString*)columnName findByFilter:(NSString*)columnFilter showAll:(BOOL)showAll orderBy:(NSString*)orderBy;


@end
