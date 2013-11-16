//
//  ListViewController.m
//  testApp
//
//  Created by Lucy Hutcheson on 12/10/12.
//  Copyright (c) 2012 Lucy Hutcheson. All rights reserved.
//

#import "ListViewController.h"
#import <QuartzCore/QuartzCore.h>
#import "AddViewController.h"
#import <Parse/Parse.h>
#import "DBManager.h"
#import "disciple.h"


@interface ListViewController ()

@end

@implementation ListViewController
@synthesize addButton;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
        self.title = NSLocalizedString(@"Disciples", @"Disciples");
    }
    return self;
}

- (void)viewDidLoad
{
    
    // Green Navigation Bar
    self.navigationController.navigationBar.tintColor = [UIColor colorWithRed:103.0/255.0 green:158.0/255.0 blue:8.0/255.0 alpha:1.0];
    
    // Edit Button
    UIBarButtonItem *editButton = [[UIBarButtonItem alloc] initWithTitle:NSLocalizedString(@"Edit", @"") style:UIBarButtonItemStyleBordered  target:self action:@selector(goEdit:)];
    self.navigationItem.rightBarButtonItem = editButton;

    // Round out custom buttons
    CALayer *addBtnLayer = [self.addButton layer];
    [addBtnLayer setMasksToBounds:YES];
    [addBtnLayer setCornerRadius:7.0f];
    
    // set table view default to false
    [myTableView setEditing:false];

    // LOAD DATA FROM LOCAL SQL AND REFRESH TABLE
    [self reloadMyTableData];

    
    [super viewDidLoad];
	// Do any additional setup after loading the view.
}

-(BOOL)reloadMyTableData
{
    resultArray = [[DBManager getSharedInstance] findByColumn:NULL findByFilter:NULL showAll:YES orderBy:NULL];
    if (resultArray != nil) {
        [myTableView reloadData];
        return YES;
    }
    return NO;
}


//-(void)onSave:(NSString*)myFirst last:(NSString*)myLast phone:(NSString*)myPhone email:(NSString*)myEmail
-(void)onSave:(NSString*)myFirst
{
    NSLog(@"hello %@", myFirst);
}


- (void)goEdit:(id)sender
{
    if (myTableView.editing == false)
    {
        [myTableView setEditing: true];
        
        // Set button to done to indicate to user to press to get out of edit mode
        self.navigationItem.rightBarButtonItem.title = @"Done";

    }
    else
    {
        [myTableView setEditing: false];
        
        // Set button back to edit for user to see
        self.navigationItem.rightBarButtonItem.title = @"Edit";
    }
}

- (UITableViewCellEditingStyle)tableView:(UITableView *)tableView editingStyleForRowAtIndexPath:(NSIndexPath *)indexPath
{
    // set up editing style to delete
    return UITableViewCellEditingStyleDelete;
}

- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSLog(@"delete row %@", ((disciple *) [resultArray objectAtIndex:indexPath.row]).first);
    [[DBManager getSharedInstance] deleteData:((disciple *) [resultArray objectAtIndex:indexPath.row]).first last:((disciple *) [resultArray objectAtIndex:indexPath.row]).last email:((disciple *) [resultArray objectAtIndex:indexPath.row]).email];
    [self reloadMyTableData];
        
}

-(IBAction)sortTable:(id)sender
{
    
    if ([sender tag] == 0)
    {
        // do something here
        resultArray = [[DBManager getSharedInstance] findByColumn:NULL findByFilter:NULL showAll:NULL orderBy:@"first"];
    }
    else if ([sender tag] == 1)
    {
        // Do some think here
        resultArray = [[DBManager getSharedInstance] findByColumn:NULL findByFilter:NULL showAll:NULL orderBy:@"last"];
    }
    else
    {
        resultArray = [[DBManager getSharedInstance] findByColumn:NULL findByFilter:NULL showAll:NULL orderBy:@"age"];
    }
    [myTableView reloadData];
    
}


/*
- (void)tableView:(UITableView *)tableView didHighlightRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    // Pull the related information for the selected disciple
    NSString *selectedFirst = [firstName objectAtIndex:[indexPath row]];
    NSString *selectedLast = [lastName objectAtIndex:[indexPath row]];
    NSString *selectedEmail = [email objectAtIndex:[indexPath row]];
    NSString *selectedPhone = [phone objectAtIndex:[indexPath row]];
    NSString *selectedAge = [age objectAtIndex:[indexPath row]];
    
    // Setup detail view
    DetailViewController *myDetailView = [[DetailViewController alloc] initWithNibName:@"DetailView" bundle:[NSBundle mainBundle]];
    self.detailView = myDetailView;
    
    // Setup disciple information for display
    detailView.firstSelected = selectedFirst;
    detailView.lastSelected = selectedLast;
    detailView.emailSelected = selectedEmail;
    detailView.phoneSelected = selectedPhone;
    detailView.ageSelected = selectedAge;
    
    
    // Pull up my detail view
    //[self.navigationController pushViewController:myDetailView animated:true];
}*/

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return resultArray.count;
}



- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    NSString *setupAge = [NSString stringWithFormat:@"%@",((disciple *) [resultArray objectAtIndex:indexPath.row]).age];
    
    // setup table cells
    UITableViewCell *cell = [myTableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil)
    {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:CellIdentifier];
    }
    // IF WE HAVE FILTERED OUR DATA USE THAT TO OVERRIDE THE ALLMOVIES ARRAY
    // OTHERWISE, WE JUST USE THE ALL MOVIES ARRAY
    cell.textLabel.text  = [NSString stringWithFormat:@"%@ %@, %@",((disciple *) [resultArray objectAtIndex:indexPath.row]).first, ((disciple *) [resultArray objectAtIndex:indexPath.row]).last, setupAge];
    NSString *phoneData = ((disciple *)[resultArray objectAtIndex:indexPath.row]).phone;
    NSString *emailData = ((disciple *)[resultArray objectAtIndex:indexPath.row]).email;
    cell.detailTextLabel.text = [NSString stringWithFormat:@"T: %@, E: %@", phoneData, emailData ];

    
    return cell;
}



// LET'S SYNC THE DATA
-(void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    
    NSMutableArray *objectIDs = [[NSMutableArray alloc] init];
    NSMutableArray *saveArray = [[DBManager getSharedInstance] findByColumn:NULL findByFilter:NULL showAll:YES orderBy:NULL];
    if (saveArray != nil) {
        
        // SYNC: SAVE LOCAL DATA TO PARSE.COM
        PFQuery *query = [PFQuery queryWithClassName:@"Disciple"];
        [query findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error) {
            if (!error) {
                // The find succeeded.
                NSLog(@"Successfully retrieved %d rows.", objects.count);
                
                // Do something with the found objects
                for (PFObject *object in objects) {
                    [objectIDs addObject:object.objectId];
                    [query whereKey:@"objectId" equalTo:object.objectId];
                    [object deleteInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
                        if (!error)
                        {
                            for (int i=0; i<saveArray.count; i++)
                            {
                                NSLog(@"Successfully deleted: %@", object.objectId);
                                
                            }
                        }
                        else
                        {
                            NSLog(@"Error: %@ %@", error, [error userInfo]);
                        }
                    }];
                }
                [self uploadData];
                [self reloadMyTableData];

            } else {
                // Log details of the failure
                NSLog(@"Error: %@ %@", error, [error userInfo]);
            }
        }];
    }
}



-(void)uploadData
{
    NSLog(@"%@", ((disciple*)[resultArray objectAtIndex:0]).first);
    
    for (int i=0; i<resultArray.count; i++)
    {
        // SAVE INITIAL DATA TO PARSE.COM
        NSString *firstSave = ((disciple*)[resultArray objectAtIndex:i]).first;
        NSString *lastSave = ((disciple*)[resultArray objectAtIndex:i]).last;
        NSString *emailSave = ((disciple*)[resultArray objectAtIndex:i]).email;
        NSString *phoneSave = ((disciple*)[resultArray objectAtIndex:i]).phone;
        NSNumber *ageNum = ((disciple*)[resultArray objectAtIndex:i]).age;
        
        // SAVE INITIAL DATA TO PARSE.COM
        PFObject *disciples = [PFObject objectWithClassName:@"Disciple"];
        disciples[@"first"] = firstSave;
        disciples[@"last"] = lastSave;
        disciples[@"email"] = emailSave;
        disciples[@"phone"] = phoneSave;
        disciples[@"age"] = ageNum;
        [disciples saveInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
            if (!error) {
                // The gameScore saved successfully.
            } else {
                // There was an error saving the gameScore.
                NSLog(@"%@", error);
            }
        }];
    }

}

-(IBAction)onClick:(id)sender
{
    UIButton *button = (UIButton *)sender;
    if (button != nil)
    {
        AddViewController *addView = [[AddViewController alloc] initWithNibName:@"AddView" bundle:nil];
        if (addView != nil)
        {
            [self.navigationController pushViewController:addView animated:true];
        }
        
    }
}



    
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
