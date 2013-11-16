//
//  DBManager.m
//  testApp
//
//  Created by Lucy Hutcheson on 11/12/13.
//  Copyright (c) 2013 Lucy Hutcheson. All rights reserved.
//

#import "DBManager.h"
#import "disciple.h"

static DBManager *sharedInstance = nil;
static sqlite3 *database = nil;
static sqlite3_stmt *statement = nil;


@implementation DBManager

+(DBManager*)getSharedInstance
{
    if (!sharedInstance) {
        sharedInstance = [[super allocWithZone:NULL]init];
        [sharedInstance createDB];
    }
    return sharedInstance;
}

-(BOOL)createDB
{
    NSString *docsDir;
    NSArray *dirPaths;
    
    dirPaths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    docsDir = dirPaths[0];
    
    databasePath = [[NSString alloc] initWithString:[docsDir stringByAppendingPathComponent: @"disciples.db"]];
    BOOL isSuccess = YES;
    NSFileManager *filemgr = [NSFileManager defaultManager];
    
    if ([filemgr fileExistsAtPath: databasePath ] == NO)
    {
        const char *dbpath = [databasePath UTF8String];
        if (sqlite3_open(dbpath, &database) == SQLITE_OK)
        {
            char *errMsg;
            const char *sql_stmt = "create table if not exists disciple (id integer primary key, first text, last text, phone text, email text, age integer, timestamp integer)";
            if (sqlite3_exec(database, sql_stmt, NULL, NULL, &errMsg) != SQLITE_OK)
            {
                isSuccess = NO;
                NSLog(@"Failed to create table");
            }
            sqlite3_close(database);
            return  isSuccess;
        }
        else {
            isSuccess = NO;
            NSLog(@"Failed to open/create database");
        }
    }
    return isSuccess;
}

- (BOOL)saveData:(NSString*)first last:(NSString*)last email:(NSString*)email phone:(NSString*)phone age:(NSNumber *)age
{
    const char *dbpath = [databasePath UTF8String];
    if (sqlite3_open(dbpath, &database) == SQLITE_OK)
    {
        NSDate *dateY = [NSDate dateWithTimeIntervalSinceNow:-86400];
        NSNumber *timestamp = [NSNumber numberWithDouble:[dateY timeIntervalSince1970]];
        
        NSString *insertSQL = [NSString stringWithFormat:@"insert into disciple (first, last, email, phone, age, timestamp) values (\"%@\", \"%@\", \"%@\", \"%@\", %@, %@)", first, last, email, phone, age, timestamp];
        const char *insert_stmt = [insertSQL UTF8String];
        sqlite3_prepare_v2(database, insert_stmt,-1, &statement, NULL);
        if (sqlite3_step(statement) == SQLITE_DONE)
        {
            return YES;
        }
        else
        {
            return NO;
        }
        sqlite3_reset(statement);
    }
    return NO;
}



/*- (NSMutableArray*) findByMovie:(NSString*)movieTitle findByFilter:(NSString*)movieFilter showAll:(BOOL)showAll
{
    const char *dbpath = [databasePath UTF8String];
    if (sqlite3_open(dbpath, &database) == SQLITE_OK)
    {
        if (showAll)
        {
            // FORMAT SQL STATEMENT BASED ON ENTERED TITLE
            querySQL = @"select title, mpaa, runtime from rottentomatoes ";
        }
        else
        {
            // FORMAT SQL STATEMENT BASED ON ENTERED TITLE
            querySQL = [NSString stringWithFormat:@"select title, mpaa, runtime from rottentomatoes where %@=\"%@\"",movieFilter, movieTitle];
        }
        const char *query_stmt = [querySQL UTF8String];
        
        NSMutableArray *resultArray = [[NSMutableArray alloc]init];
        if (sqlite3_prepare_v2(database, query_stmt, -1, &statement, NULL) == SQLITE_OK)
        {
            if (sqlite3_step(statement) == SQLITE_ROW)
            {
                while (sqlite3_step(statement) == SQLITE_ROW)
                {
                    //NSString *title = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 0)];
                    //NSString *mpaa = [[NSString alloc] initWithUTF8String: (const char *) sqlite3_column_text(statement, 1)];
                    //NSString *runtime = [[NSString alloc]initWithUTF8String:(const char *) sqlite3_column_text(statement, 2)];
                    
                    Movie *myMovie = [[Movie alloc] init];
                    myMovie.title = title;
                    myMovie.mpaa = mpaa;
                    myMovie.runtime = runtime;
                    [resultArray addObject:myMovie];
                }
                return resultArray;
            }
            else
            {
                NSLog(@"Not found");
                return nil;
            }
            sqlite3_reset(statement);
        }
    }
    return nil;
}*/


- (NSMutableArray*) findByColumn:(NSString*)columnName findByFilter:(NSString*)columnFilter showAll:(BOOL)showAll orderBy:(NSString*)orderBy
 {
     const char *dbpath = [databasePath UTF8String];
     if (sqlite3_open(dbpath, &database) == SQLITE_OK)
     {
         if (showAll)
         {
             // FORMAT SQL STATEMENT BASED ON ENTERED TITLE
             querySQL = @"SELECT * FROM disciple";
         }
         else if (orderBy != nil)
         {
             querySQL = [NSString stringWithFormat:@"select * from disciple ORDER BY %@", orderBy];
         }
         else
         {
             // FORMAT SQL STATEMENT BASED ON ENTERED TITLE
             querySQL = [NSString stringWithFormat:@"select %@ from disciple where %@=\"%@\"",columnName, columnName, columnFilter];
         }
         const char *query_stmt = [querySQL UTF8String];
 
         NSMutableArray *resultArray = [[NSMutableArray alloc]init];
         if (sqlite3_prepare_v2(database, query_stmt, -1, &statement, NULL) == SQLITE_OK)
         {
             if (sqlite3_step(statement) == SQLITE_ROW)
             {
                 while (sqlite3_step(statement) == SQLITE_ROW)
                 {
                     NSInteger dataID = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 0)];
                     NSString *dataFirst = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 1)];
                     NSString *dataLast = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 2)];
                     NSString *dataPhone = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 3)];
                     NSString *dataEmail = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 4)];
                     NSString *dataAge1 = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 5)];
                     NSNumber *dataAge = [NSNumber numberWithInt:[dataAge1 intValue]];

                     
                     disciple *currentDisciple = [[disciple alloc] init];
                     currentDisciple.discipleID = dataID;
                     currentDisciple.first = dataFirst;
                     currentDisciple.last = dataLast;
                     currentDisciple.email = dataEmail;
                     currentDisciple.phone = dataPhone;
                     currentDisciple.age = dataAge;
                     [resultArray addObject:currentDisciple];
                 }
                 return resultArray;
             }
             else
             {
                 NSLog(@"Not found");
                 return nil;
             }
             sqlite3_reset(statement);
         }
     }
     return nil;
 }


- (BOOL)deleteData:(NSString*)first last:(NSString*)last email:(NSString*)email
{
    const char *dbpath = [databasePath UTF8String];
    if (sqlite3_open(dbpath, &database) == SQLITE_OK)
    {
        NSString *insertSQL = [NSString stringWithFormat:@"DELETE FROM disciple WHERE first =\"%@\" AND last=\"%@\" AND email=\"%@\" ", first, last, email];
        const char *insert_stmt = [insertSQL UTF8String];
        sqlite3_prepare_v2(database, insert_stmt,-1, &statement, NULL);
        if (sqlite3_step(statement) == SQLITE_DONE)
        {
            return YES;
        }
        else
        {
            return NO;
        }
        
        sqlite3_reset(statement);
    }
    return NO;
}


@end
